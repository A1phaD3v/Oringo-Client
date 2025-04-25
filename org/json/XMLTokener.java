// 
// Decompiled by Procyon v0.5.36
// 

package org.json;

import java.util.HashMap;

public class XMLTokener extends JSONTokener
{
    public static final HashMap<String, Character> entity;
    
    public XMLTokener(final String s) {
        super(s);
    }
    
    public String nextCDATA() throws JSONException {
        final StringBuilder sb = new StringBuilder();
        while (this.more()) {
            final char c = this.next();
            sb.append(c);
            final int i = sb.length() - 3;
            if (i >= 0 && sb.charAt(i) == ']' && sb.charAt(i + 1) == ']' && sb.charAt(i + 2) == '>') {
                sb.setLength(i);
                return sb.toString();
            }
        }
        throw this.syntaxError("Unclosed CDATA");
    }
    
    public Object nextContent() throws JSONException {
        char c;
        do {
            c = this.next();
        } while (Character.isWhitespace(c));
        if (c == '\0') {
            return null;
        }
        if (c == '<') {
            return XML.LT;
        }
        final StringBuilder sb = new StringBuilder();
        while (c != '\0') {
            if (c == '<') {
                this.back();
                return sb.toString().trim();
            }
            if (c == '&') {
                sb.append(this.nextEntity(c));
            }
            else {
                sb.append(c);
            }
            c = this.next();
        }
        return sb.toString().trim();
    }
    
    public Object nextEntity(final char ampersand) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        char c;
        while (true) {
            c = this.next();
            if (!Character.isLetterOrDigit(c) && c != '#') {
                break;
            }
            sb.append(Character.toLowerCase(c));
        }
        if (c == ';') {
            final String string = sb.toString();
            return unescapeEntity(string);
        }
        throw this.syntaxError("Missing ';' in XML entity: &" + (Object)sb);
    }
    
    static String unescapeEntity(final String e) {
        if (e == null || e.isEmpty()) {
            return "";
        }
        if (e.charAt(0) == '#') {
            int cp;
            if (e.charAt(1) == 'x') {
                cp = Integer.parseInt(e.substring(2), 16);
            }
            else {
                cp = Integer.parseInt(e.substring(1));
            }
            return new String(new int[] { cp }, 0, 1);
        }
        final Character knownEntity = XMLTokener.entity.get(e);
        if (knownEntity == null) {
            return '&' + e + ';';
        }
        return knownEntity.toString();
    }
    
    public Object nextMeta() throws JSONException {
        char c;
        do {
            c = this.next();
        } while (Character.isWhitespace(c));
        switch (c) {
            case '\0': {
                throw this.syntaxError("Misshaped meta tag");
            }
            case '<': {
                return XML.LT;
            }
            case '>': {
                return XML.GT;
            }
            case '/': {
                return XML.SLASH;
            }
            case '=': {
                return XML.EQ;
            }
            case '!': {
                return XML.BANG;
            }
            case '?': {
                return XML.QUEST;
            }
            case '\"':
            case '\'': {
                final char q = c;
                do {
                    c = this.next();
                    if (c == '\0') {
                        throw this.syntaxError("Unterminated string");
                    }
                } while (c != q);
                return Boolean.TRUE;
            }
            default: {
                while (true) {
                    c = this.next();
                    if (Character.isWhitespace(c)) {
                        return Boolean.TRUE;
                    }
                    switch (c) {
                        case '\0':
                        case '!':
                        case '\"':
                        case '\'':
                        case '/':
                        case '<':
                        case '=':
                        case '>':
                        case '?': {
                            this.back();
                            return Boolean.TRUE;
                        }
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public Object nextToken() throws JSONException {
        char c;
        do {
            c = this.next();
        } while (Character.isWhitespace(c));
        switch (c) {
            case '\0': {
                throw this.syntaxError("Misshaped element");
            }
            case '<': {
                throw this.syntaxError("Misplaced '<'");
            }
            case '>': {
                return XML.GT;
            }
            case '/': {
                return XML.SLASH;
            }
            case '=': {
                return XML.EQ;
            }
            case '!': {
                return XML.BANG;
            }
            case '?': {
                return XML.QUEST;
            }
            case '\"':
            case '\'': {
                final char q = c;
                final StringBuilder sb = new StringBuilder();
                while (true) {
                    c = this.next();
                    if (c == '\0') {
                        throw this.syntaxError("Unterminated string");
                    }
                    if (c == q) {
                        return sb.toString();
                    }
                    if (c == '&') {
                        sb.append(this.nextEntity(c));
                    }
                    else {
                        sb.append(c);
                    }
                }
                break;
            }
            default: {
                final StringBuilder sb = new StringBuilder();
                while (true) {
                    sb.append(c);
                    c = this.next();
                    if (Character.isWhitespace(c)) {
                        return sb.toString();
                    }
                    switch (c) {
                        case '\0': {
                            return sb.toString();
                        }
                        case '!':
                        case '/':
                        case '=':
                        case '>':
                        case '?':
                        case '[':
                        case ']': {
                            this.back();
                            return sb.toString();
                        }
                        case '\"':
                        case '\'':
                        case '<': {
                            throw this.syntaxError("Bad character in a name");
                        }
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public boolean skipPast(final String to) throws JSONException {
        int offset = 0;
        final int length = to.length();
        final char[] circle = new char[length];
        for (int i = 0; i < length; ++i) {
            final char c = this.next();
            if (c == '\0') {
                return false;
            }
            circle[i] = c;
        }
        while (true) {
            int j = offset;
            boolean b = true;
            for (int i = 0; i < length; ++i) {
                if (circle[j] != to.charAt(i)) {
                    b = false;
                    break;
                }
                if (++j >= length) {
                    j -= length;
                }
            }
            if (b) {
                return true;
            }
            final char c = this.next();
            if (c == '\0') {
                return false;
            }
            circle[offset] = c;
            if (++offset < length) {
                continue;
            }
            offset -= length;
        }
    }
    
    static {
        (entity = new HashMap<String, Character>(8)).put("amp", XML.AMP);
        XMLTokener.entity.put("apos", XML.APOS);
        XMLTokener.entity.put("gt", XML.GT);
        XMLTokener.entity.put("lt", XML.LT);
        XMLTokener.entity.put("quot", XML.QUOT);
    }
}
