/**
 * JEBML - Java library to read/write EBML/Matroska elements.
 * Copyright (C) 2004 Jory Stone <jebml@jory.info>
 * Based on Javatroska (C) 2002 John Cannon <spyder@matroska.org>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.ebml;

import java.nio.ByteBuffer;
import java.util.logging.Level;

/*
 * UnsignedIntegerElement.java
 *
 * Created on February 15, 2003, 6:27 PM
 */
/**
 * Basic class for the Unsigned Integer data type in EBML.
 * 
 * @author John Cannon
 */
public class UnsignedIntegerElement extends BinaryElement
{

  public UnsignedIntegerElement(final byte[] typeID)
  {
    super(typeID);
  }

  public UnsignedIntegerElement()
  {
    super();
  }

  public void setValue(final long value)
  {
    final ByteBuffer buf = ByteBuffer.wrap(packIntUnsigned(value));
    LOGGER.log(Level.FINER, "Setting value [" + value + "] to [" + EBMLReader.bytesToHex(buf.array()) + "]");
    setData(buf);
  }

  public long getValue()
  {
    return EBMLReader.parseEBMLCode(data.duplicate());
  }
}
