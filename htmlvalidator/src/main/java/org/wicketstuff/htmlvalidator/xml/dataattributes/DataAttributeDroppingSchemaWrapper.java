/*
 * Copyright (c) 2008 Mozilla Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package org.wicketstuff.htmlvalidator.xml.dataattributes;

import com.thaiopensource.util.PropertyMap;
import com.thaiopensource.validate.Schema;
import com.thaiopensource.validate.Validator;

public class DataAttributeDroppingSchemaWrapper implements Schema {
    
    private final Schema delegate;

    /**
     * @param delegate
     */
    public DataAttributeDroppingSchemaWrapper(Schema delegate) {
        this.delegate = delegate;
    }

    /**
     * @param properties
     * @return
     * @see com.thaiopensource.validate.Schema#createValidator(com.thaiopensource.util.PropertyMap)
     */
    public Validator createValidator(PropertyMap properties) {
        return new DataAttributeDroppingValidatorWrapper(delegate.createValidator(properties), properties);
    }

    /**
     * @return
     * @see com.thaiopensource.validate.Schema#getProperties()
     */
    public PropertyMap getProperties() {
        return delegate.getProperties();
    }
    
}
