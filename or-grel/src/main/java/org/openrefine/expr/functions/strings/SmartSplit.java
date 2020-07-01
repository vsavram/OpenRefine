/*

Copyright 2010, Google Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,           
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY           
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package org.openrefine.expr.functions.strings;

import java.io.IOException;
import java.util.Locale;

import org.openrefine.grel.ControlFunctionRegistry;
import org.openrefine.grel.PureFunction;

import org.openrefine.expr.EvalError;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

public class SmartSplit extends PureFunction {

    private static final long serialVersionUID = 2560697330486313877L;

    static final protected CSVParser s_tabParser = buildParser('\t');
    
    static final protected CSVParser s_commaParser = buildParser(',');
    
    protected static CSVParser buildParser(char separator) {
    	return new CSVParserBuilder()
		.withSeparator(separator)
		.withQuoteChar(CSVParser.DEFAULT_QUOTE_CHARACTER)
		.withEscapeChar(CSVParser.DEFAULT_ESCAPE_CHARACTER)
		.withStrictQuotes(CSVParser.DEFAULT_STRICT_QUOTES)
		.withIgnoreLeadingWhiteSpace(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE)
		.withIgnoreQuotations(false)
		.withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
		.withErrorLocale(Locale.US)
		.build();
    }

    @Override
    public Object call(Object[] args) {
        if (args.length >= 1 && args.length <= 2) {
            CSVParser parser = null;
            
            Object v = args[0];
            String s = v.toString();
            
            if (args.length > 1) {
            	if(args[1].toString().length() == 1) {
            		String sep = args[1].toString();
                	parser = buildParser(sep.charAt(0));
            	} else {
            		return new EvalError(ControlFunctionRegistry.getFunctionName(this) + " only supports single-character separators");
            	}
            }
            
            if (parser == null) {
                int tab = s.indexOf('\t');
                if (tab >= 0) {
                    parser = s_tabParser;
                } else {
                    parser = s_commaParser;
                }
            }
            
            try {
                return parser.parseLine(s);
            } catch (IOException e) {
                return new EvalError(ControlFunctionRegistry.getFunctionName(this) + " error: " + e.getMessage());
            }
        }
        return new EvalError(ControlFunctionRegistry.getFunctionName(this) + " expects 1 or 2 strings");
    }
    
    @Override
    public String getDescription() {
        return "Returns the array of strings obtained by splitting s with separator sep. Handles quotes properly. Guesses tab or comma separator if \"sep\" is not given.";
    }
    
    @Override
    public String getParams() {
        return "string s, optional string sep";
    }
    
    @Override
    public String getReturns() {
        return "array";
    }
}
