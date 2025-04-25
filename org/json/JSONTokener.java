// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.io.IOException;
import java.io.StringReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.Reader;

public class JSONTokener
{
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private final Reader reader;
    private boolean usePrevious;
    private long characterPreviousLine;
    
    public JSONTokener(final Reader reader) {
        this.reader = (reader.markSupported() ? reader : new BufferedReader(reader));
        this.eof = false;
        this.usePrevious = false;
        this.previous = '\0';
        this.index = 0L;
        this.character = 1L;
        this.characterPreviousLine = 0L;
        this.line = 1L;
    }
    
    public JSONTokener(final InputStream inputStream) {
        this(new InputStreamReader(inputStream));
    }
    
    public JSONTokener(final String s) {
        this(new StringReader(s));
    }
    
    public void back() throws JSONException {
        if (this.usePrevious || this.index <= 0L) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.decrementIndexes();
        this.usePrevious = true;
        this.eof = false;
    }
    
    private void decrementIndexes() {
        --this.index;
        if (this.previous == '\r' || this.previous == '\n') {
            --this.line;
            this.character = this.characterPreviousLine;
        }
        else if (this.character > 0L) {
            --this.character;
        }
    }
    
    public static int dehexchar(final char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'W';
        }
        return -1;
    }
    
    public boolean end() {
        return this.eof && !this.usePrevious;
    }
    
    public boolean more() throws JSONException {
        if (this.usePrevious) {
            return true;
        }
        try {
            this.reader.mark(1);
        }
        catch (IOException e) {
            throw new JSONException("Unable to preserve stream position", e);
        }
        try {
            if (this.reader.read() <= 0) {
                this.eof = true;
                return false;
            }
            this.reader.reset();
        }
        catch (IOException e) {
            throw new JSONException("Unable to read the next character from the stream", e);
        }
        return true;
    }
    
    public char next() throws JSONException {
        int c;
        if (this.usePrevious) {
            this.usePrevious = false;
            c = this.previous;
        }
        else {
            try {
                c = this.reader.read();
            }
            catch (IOException exception) {
                throw new JSONException(exception);
            }
        }
        if (c <= 0) {
            this.eof = true;
            return '\0';
        }
        this.incrementIndexes(c);
        return this.previous = (char)c;
    }
    
    private void incrementIndexes(final int c) {
        if (c > 0) {
            ++this.index;
            if (c == 13) {
                ++this.line;
                this.characterPreviousLine = this.character;
                this.character = 0L;
            }
            else if (c == 10) {
                if (this.previous != '\r') {
                    ++this.line;
                    this.characterPreviousLine = this.character;
                }
                this.character = 0L;
            }
            else {
                ++this.character;
            }
        }
    }
    
    public char next(final char c) throws JSONException {
        final char n = this.next();
        if (n == c) {
            return n;
        }
        if (n > '\0') {
            throw this.syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
        }
        throw this.syntaxError("Expected '" + c + "' and instead saw ''");
    }
    
    public String next(final int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        final char[] chars = new char[n];
        for (int pos = 0; pos < n; ++pos) {
            chars[pos] = this.next();
            if (this.end()) {
                throw this.syntaxError("Substring bounds error");
            }
        }
        return new String(chars);
    }
    
    public char nextClean() throws JSONException {
        char c;
        do {
            c = this.next();
        } while (c != '\0' && c <= ' ');
        return c;
    }
    
    public String nextString(final char quote) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            char c = this.next();
            switch (c) {
                case '\0':
                case '\n':
                case '\r': {
                    throw this.syntaxError("Unterminated string");
                }
                case '\\': {
                    c = this.next();
                    switch (c) {
                        case 'b': {
                            sb.append('\b');
                            continue;
                        }
                        case 't': {
                            sb.append('\t');
                            continue;
                        }
                        case 'n': {
                            sb.append('\n');
                            continue;
                        }
                        case 'f': {
                            sb.append('\f');
                            continue;
                        }
                        case 'r': {
                            sb.append('\r');
                            continue;
                        }
                        case 'u': {
                            try {
                                sb.append((char)Integer.parseInt(this.next(4), 16));
                                continue;
                            }
                            catch (NumberFormatException e) {
                                throw this.syntaxError("Illegal escape.", e);
                            }
                        }
                        case '\"':
                        case '\'':
                        case '/':
                        case '\\': {
                            sb.append(c);
                            continue;
                        }
                        default: {
                            throw this.syntaxError("Illegal escape.");
                        }
                    }
                    break;
                }
                default: {
                    if (c == quote) {
                        return sb.toString();
                    }
                    sb.append(c);
                    continue;
                }
            }
        }
    }
    
    public String nextTo(final char delimiter) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        char c;
        while (true) {
            c = this.next();
            if (c == delimiter || c == '\0' || c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if (c != '\0') {
            this.back();
        }
        return sb.toString().trim();
    }
    
    public String nextTo(final String delimiters) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        char c;
        while (true) {
            c = this.next();
            if (delimiters.indexOf(c) >= 0 || c == '\0' || c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if (c != '\0') {
            this.back();
        }
        return sb.toString().trim();
    }
    
    public Object nextValue() throws JSONException {
        char c = this.nextClean();
        switch (c) {
            case '\"':
            case '\'': {
                return this.nextString(c);
            }
            case '{': {
                this.back();
                return new JSONObject(this);
            }
            case '[': {
                this.back();
                return new JSONArray(this);
            }
            default: {
                final StringBuilder sb = new StringBuilder();
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = this.next();
                }
                this.back();
                final String string = sb.toString().trim();
                if ("".equals(string)) {
                    throw this.syntaxError("Missing value");
                }
                return JSONObject.stringToValue(string);
            }
        }
    }
    
    public char skipTo(final char to) throws JSONException {
        char c;
        try {
            final long startIndex = this.index;
            final long startCharacter = this.character;
            final long startLine = this.line;
            this.reader.mark(1000000);
            do {
                c = this.next();
                if (c == '\0') {
                    this.reader.reset();
                    this.index = startIndex;
                    this.character = startCharacter;
                    this.line = startLine;
                    return '\0';
                }
            } while (c != to);
            this.reader.mark(1);
        }
        catch (IOException exception) {
            throw new JSONException(exception);
        }
        this.back();
        return c;
    }
    
    public JSONException syntaxError(final String message) {
        return new JSONException(message + this.toString());
    }
    
    public JSONException syntaxError(final String message, final Throwable causedBy) {
        return new JSONException(message + this.toString(), causedBy);
    }
    
    @Override
    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}
