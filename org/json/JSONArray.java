// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.util.List;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Map;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class JSONArray implements Iterable<Object>
{
    private final ArrayList<Object> myArrayList;
    
    public JSONArray() {
        this.myArrayList = new ArrayList<Object>();
    }
    
    public JSONArray(final JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() == ']') {
            return;
        }
        x.back();
        while (true) {
            if (x.nextClean() == ',') {
                x.back();
                this.myArrayList.add(JSONObject.NULL);
            }
            else {
                x.back();
                this.myArrayList.add(x.nextValue());
            }
            switch (x.nextClean()) {
                case ',': {
                    if (x.nextClean() == ']') {
                        return;
                    }
                    x.back();
                    continue;
                }
                case ']': {}
                default: {
                    throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }
    
    public JSONArray(final String source) throws JSONException {
        this(new JSONTokener(source));
    }
    
    public JSONArray(final Collection<?> collection) {
        if (collection == null) {
            this.myArrayList = new ArrayList<Object>();
        }
        else {
            this.myArrayList = new ArrayList<Object>(collection.size());
            for (final Object o : collection) {
                this.myArrayList.add(JSONObject.wrap(o));
            }
        }
    }
    
    public JSONArray(final Object array) throws JSONException {
        this();
        if (array.getClass().isArray()) {
            final int length = Array.getLength(array);
            this.myArrayList.ensureCapacity(length);
            for (int i = 0; i < length; ++i) {
                this.put(JSONObject.wrap(Array.get(array, i)));
            }
            return;
        }
        throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }
    
    @Override
    public Iterator<Object> iterator() {
        return this.myArrayList.iterator();
    }
    
    public Object get(final int index) throws JSONException {
        final Object object = this.opt(index);
        if (object == null) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        return object;
    }
    
    public boolean getBoolean(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException("JSONArray[" + index + "] is not a boolean.");
    }
    
    public double getDouble(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            return (object instanceof Number) ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }
    
    public float getFloat(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            return (object instanceof Number) ? ((Number)object).floatValue() : Float.parseFloat(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }
    
    public Number getNumber(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            if (object instanceof Number) {
                return (Number)object;
            }
            return JSONObject.stringToNumber(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }
    
    public <E extends Enum<E>> E getEnum(final Class<E> clazz, final int index) throws JSONException {
        final E val = (E)this.optEnum((Class<Enum>)clazz, index);
        if (val == null) {
            throw new JSONException("JSONArray[" + index + "] is not an enum of type " + JSONObject.quote(clazz.getSimpleName()) + ".");
        }
        return val;
    }
    
    public BigDecimal getBigDecimal(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            return new BigDecimal(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] could not convert to BigDecimal.", e);
        }
    }
    
    public BigInteger getBigInteger(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            return new BigInteger(object.toString());
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] could not convert to BigInteger.", e);
        }
    }
    
    public int getInt(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            return (object instanceof Number) ? ((Number)object).intValue() : Integer.parseInt((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }
    
    public JSONArray getJSONArray(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof JSONArray) {
            return (JSONArray)object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
    }
    
    public JSONObject getJSONObject(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof JSONObject) {
            return (JSONObject)object;
        }
        throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
    }
    
    public long getLong(final int index) throws JSONException {
        final Object object = this.get(index);
        try {
            return (object instanceof Number) ? ((Number)object).longValue() : Long.parseLong((String)object);
        }
        catch (Exception e) {
            throw new JSONException("JSONArray[" + index + "] is not a number.", e);
        }
    }
    
    public String getString(final int index) throws JSONException {
        final Object object = this.get(index);
        if (object instanceof String) {
            return (String)object;
        }
        throw new JSONException("JSONArray[" + index + "] not a string.");
    }
    
    public boolean isNull(final int index) {
        return JSONObject.NULL.equals(this.opt(index));
    }
    
    public String join(final String separator) throws JSONException {
        final int len = this.length();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return sb.toString();
    }
    
    public int length() {
        return this.myArrayList.size();
    }
    
    public Object opt(final int index) {
        return (index < 0 || index >= this.length()) ? null : this.myArrayList.get(index);
    }
    
    public boolean optBoolean(final int index) {
        return this.optBoolean(index, false);
    }
    
    public boolean optBoolean(final int index, final boolean defaultValue) {
        try {
            return this.getBoolean(index);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public double optDouble(final int index) {
        return this.optDouble(index, Double.NaN);
    }
    
    public double optDouble(final int index, final double defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).doubleValue();
        }
        if (val instanceof String) {
            try {
                return Double.parseDouble((String)val);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public float optFloat(final int index) {
        return this.optFloat(index, Float.NaN);
    }
    
    public float optFloat(final int index, final float defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).floatValue();
        }
        if (val instanceof String) {
            try {
                return Float.parseFloat((String)val);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public int optInt(final int index) {
        return this.optInt(index, 0);
    }
    
    public int optInt(final int index, final int defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).intValue();
        }
        if (val instanceof String) {
            try {
                return new BigDecimal(val.toString()).intValue();
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> clazz, final int index) {
        return this.optEnum(clazz, index, (E)null);
    }
    
    public <E extends Enum<E>> E optEnum(final Class<E> clazz, final int index, final E defaultValue) {
        try {
            final Object val = this.opt(index);
            if (JSONObject.NULL.equals(val)) {
                return defaultValue;
            }
            if (clazz.isAssignableFrom(val.getClass())) {
                final E myE = (E)val;
                return myE;
            }
            return Enum.valueOf(clazz, val.toString());
        }
        catch (IllegalArgumentException e) {
            return defaultValue;
        }
        catch (NullPointerException e2) {
            return defaultValue;
        }
    }
    
    public BigInteger optBigInteger(final int index, final BigInteger defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof BigInteger) {
            return (BigInteger)val;
        }
        if (val instanceof BigDecimal) {
            return ((BigDecimal)val).toBigInteger();
        }
        if (val instanceof Double || val instanceof Float) {
            return new BigDecimal(((Number)val).doubleValue()).toBigInteger();
        }
        if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte) {
            return BigInteger.valueOf(((Number)val).longValue());
        }
        try {
            final String valStr = val.toString();
            if (JSONObject.isDecimalNotation(valStr)) {
                return new BigDecimal(valStr).toBigInteger();
            }
            return new BigInteger(valStr);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public BigDecimal optBigDecimal(final int index, final BigDecimal defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof BigDecimal) {
            return (BigDecimal)val;
        }
        if (val instanceof BigInteger) {
            return new BigDecimal((BigInteger)val);
        }
        if (val instanceof Double || val instanceof Float) {
            return new BigDecimal(((Number)val).doubleValue());
        }
        if (val instanceof Long || val instanceof Integer || val instanceof Short || val instanceof Byte) {
            return new BigDecimal(((Number)val).longValue());
        }
        try {
            return new BigDecimal(val.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public JSONArray optJSONArray(final int index) {
        final Object o = this.opt(index);
        return (o instanceof JSONArray) ? ((JSONArray)o) : null;
    }
    
    public JSONObject optJSONObject(final int index) {
        final Object o = this.opt(index);
        return (o instanceof JSONObject) ? ((JSONObject)o) : null;
    }
    
    public long optLong(final int index) {
        return this.optLong(index, 0L);
    }
    
    public long optLong(final int index, final long defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return ((Number)val).longValue();
        }
        if (val instanceof String) {
            try {
                return new BigDecimal(val.toString()).longValue();
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public Number optNumber(final int index) {
        return this.optNumber(index, null);
    }
    
    public Number optNumber(final int index, final Number defaultValue) {
        final Object val = this.opt(index);
        if (JSONObject.NULL.equals(val)) {
            return defaultValue;
        }
        if (val instanceof Number) {
            return (Number)val;
        }
        if (val instanceof String) {
            try {
                return JSONObject.stringToNumber((String)val);
            }
            catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public String optString(final int index) {
        return this.optString(index, "");
    }
    
    public String optString(final int index, final String defaultValue) {
        final Object object = this.opt(index);
        return JSONObject.NULL.equals(object) ? defaultValue : object.toString();
    }
    
    public JSONArray put(final boolean value) {
        this.put(value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONArray put(final Collection<?> value) {
        this.put(new JSONArray(value));
        return this;
    }
    
    public JSONArray put(final double value) throws JSONException {
        final Double d = new Double(value);
        JSONObject.testValidity(d);
        this.put(d);
        return this;
    }
    
    public JSONArray put(final int value) {
        this.put(new Integer(value));
        return this;
    }
    
    public JSONArray put(final long value) {
        this.put(new Long(value));
        return this;
    }
    
    public JSONArray put(final Map<?, ?> value) {
        this.put(new JSONObject(value));
        return this;
    }
    
    public JSONArray put(final Object value) {
        this.myArrayList.add(value);
        return this;
    }
    
    public JSONArray put(final int index, final boolean value) throws JSONException {
        this.put(index, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }
    
    public JSONArray put(final int index, final Collection<?> value) throws JSONException {
        this.put(index, new JSONArray(value));
        return this;
    }
    
    public JSONArray put(final int index, final double value) throws JSONException {
        this.put(index, new Double(value));
        return this;
    }
    
    public JSONArray put(final int index, final int value) throws JSONException {
        this.put(index, new Integer(value));
        return this;
    }
    
    public JSONArray put(final int index, final long value) throws JSONException {
        this.put(index, new Long(value));
        return this;
    }
    
    public JSONArray put(final int index, final Map<?, ?> value) throws JSONException {
        this.put(index, new JSONObject(value));
        return this;
    }
    
    public JSONArray put(final int index, final Object value) throws JSONException {
        JSONObject.testValidity(value);
        if (index < 0) {
            throw new JSONException("JSONArray[" + index + "] not found.");
        }
        if (index < this.length()) {
            this.myArrayList.set(index, value);
        }
        else if (index == this.length()) {
            this.put(value);
        }
        else {
            this.myArrayList.ensureCapacity(index + 1);
            while (index != this.length()) {
                this.put(JSONObject.NULL);
            }
            this.put(value);
        }
        return this;
    }
    
    public Object query(final String jsonPointer) {
        return this.query(new JSONPointer(jsonPointer));
    }
    
    public Object query(final JSONPointer jsonPointer) {
        return jsonPointer.queryFrom(this);
    }
    
    public Object optQuery(final String jsonPointer) {
        return this.optQuery(new JSONPointer(jsonPointer));
    }
    
    public Object optQuery(final JSONPointer jsonPointer) {
        try {
            return jsonPointer.queryFrom(this);
        }
        catch (JSONPointerException e) {
            return null;
        }
    }
    
    public Object remove(final int index) {
        return (index >= 0 && index < this.length()) ? this.myArrayList.remove(index) : null;
    }
    
    public boolean similar(final Object other) {
        if (!(other instanceof JSONArray)) {
            return false;
        }
        final int len = this.length();
        if (len != ((JSONArray)other).length()) {
            return false;
        }
        for (int i = 0; i < len; ++i) {
            final Object valueThis = this.myArrayList.get(i);
            final Object valueOther = ((JSONArray)other).myArrayList.get(i);
            if (valueThis == valueOther) {
                return true;
            }
            if (valueThis == null) {
                return false;
            }
            if (valueThis instanceof JSONObject) {
                if (!((JSONObject)valueThis).similar(valueOther)) {
                    return false;
                }
            }
            else if (valueThis instanceof JSONArray) {
                if (!((JSONArray)valueThis).similar(valueOther)) {
                    return false;
                }
            }
            else if (!valueThis.equals(valueOther)) {
                return false;
            }
        }
        return true;
    }
    
    public JSONObject toJSONObject(final JSONArray names) throws JSONException {
        if (names == null || names.length() == 0 || this.length() == 0) {
            return null;
        }
        final JSONObject jo = new JSONObject(names.length());
        for (int i = 0; i < names.length(); ++i) {
            jo.put(names.getString(i), this.opt(i));
        }
        return jo;
    }
    
    @Override
    public String toString() {
        try {
            return this.toString(0);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String toString(final int indentFactor) throws JSONException {
        final StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            return this.write(sw, indentFactor, 0).toString();
        }
    }
    
    public Writer write(final Writer writer) throws JSONException {
        return this.write(writer, 0, 0);
    }
    
    public Writer write(final Writer writer, final int indentFactor, final int indent) throws JSONException {
        try {
            boolean commanate = false;
            final int length = this.length();
            writer.write(91);
            Label_0178: {
                if (length == 1) {
                    try {
                        JSONObject.writeValue(writer, this.myArrayList.get(0), indentFactor, indent);
                        break Label_0178;
                    }
                    catch (Exception e) {
                        throw new JSONException("Unable to write JSONArray value at index: 0", e);
                    }
                }
                if (length != 0) {
                    final int newindent = indent + indentFactor;
                    for (int i = 0; i < length; ++i) {
                        if (commanate) {
                            writer.write(44);
                        }
                        if (indentFactor > 0) {
                            writer.write(10);
                        }
                        JSONObject.indent(writer, newindent);
                        try {
                            JSONObject.writeValue(writer, this.myArrayList.get(i), indentFactor, newindent);
                        }
                        catch (Exception e2) {
                            throw new JSONException("Unable to write JSONArray value at index: " + i, e2);
                        }
                        commanate = true;
                    }
                    if (indentFactor > 0) {
                        writer.write(10);
                    }
                    JSONObject.indent(writer, indent);
                }
            }
            writer.write(93);
            return writer;
        }
        catch (IOException e3) {
            throw new JSONException(e3);
        }
    }
    
    public List<Object> toList() {
        final List<Object> results = new ArrayList<Object>(this.myArrayList.size());
        for (final Object element : this.myArrayList) {
            if (element == null || JSONObject.NULL.equals(element)) {
                results.add(null);
            }
            else if (element instanceof JSONArray) {
                results.add(((JSONArray)element).toList());
            }
            else if (element instanceof JSONObject) {
                results.add(((JSONObject)element).toMap());
            }
            else {
                results.add(element);
            }
        }
        return results;
    }
}
