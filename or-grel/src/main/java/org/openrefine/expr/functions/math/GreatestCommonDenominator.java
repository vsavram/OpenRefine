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

package org.openrefine.expr.functions.math;

import org.openrefine.grel.ControlFunctionRegistry;
import org.openrefine.grel.PureFunction;

import org.openrefine.expr.EvalError;

public class GreatestCommonDenominator extends PureFunction {

    private static final long serialVersionUID = 5337233693109214256L;

    @Override
    public Object call(Object[] args) {
        if (args.length == 2 && args[0] != null && args[0] instanceof Number
                && args[1] != null && args[1] instanceof Number) {
            return GreatestCommonDenominator.GCD(((Number) args[0]).doubleValue(), ((Number) args[1]).doubleValue());
        }
        return new EvalError(ControlFunctionRegistry.getFunctionName(this) + " expects two numbers");
    }

    public static double GCD(double a, double b){
        return b == 0 ? a : GCD(b, a % b);
    }

    @Override
    public String getDescription() {
        return "Returns the greatest common denominator of the two numbers";
    }
    
    @Override
    public String getParams() {
        return "number d, number e";
    }
    
    @Override
    public String getReturns() {
        return "number";
    }
}