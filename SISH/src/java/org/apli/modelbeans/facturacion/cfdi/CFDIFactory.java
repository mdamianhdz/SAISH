/*
 *  Copyright 2010 BigData.mx
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apli.modelbeans.facturacion.cfdi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apli.modelbeans.facturacion.common.CFDFactory;

import com.google.common.io.ByteStreams;

public final class CFDIFactory extends CFDFactory {
  
  public static CFDI load(File file) throws Exception {
    InputStream in = new FileInputStream(file);
    try {
      return load(in);
    } finally {
      in.close();
    }
  }
  
  public static CFDI load(InputStream in) throws Exception {
    return getCFDI(in);
  }
  
  private static CFDI getCFDI(InputStream in) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ByteStreams.copy(in, baos);
    byte[] data = baos.toByteArray();
    return new CFDv32(new ByteArrayInputStream(data));
  }
}