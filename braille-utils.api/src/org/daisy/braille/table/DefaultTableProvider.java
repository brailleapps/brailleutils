/*
 * Braille Utils (C) 2010-2011 Daisy Consortium 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.daisy.braille.table;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;

/**
 * Provides a default table, for convenience.
 * @author Joel Håkansson
 */
public class DefaultTableProvider implements TableProvider {
	
	enum TableType {
		EN_US, // US computer braille, compatible with
				// "Braillo USA 6 DOT 001.00"
	};

	private final Map<TableType, Table> tables;

	/**
	 * Creates a new DefaultTableProvider
	 */
	public DefaultTableProvider() {
		tables = new HashMap<TableType, Table>(); 
		tables.put(TableType.EN_US, new EmbosserTable<TableType>("US", "Commonly used embosser table", TableType.EN_US, EightDotFallbackMethod.values()[0], '\u2800'){

			/**
			 * 
			 */
			private static final long serialVersionUID = -64524572279113677L;

			@Override
			public BrailleConverter newBrailleConverter() {
				return new EmbosserBrailleConverter(
						new String(
								" A1B'K2L@CIF/MSP\"E3H9O6R^DJG>NTQ,*5<-U8V.%[$+X!&;:4\\0Z7(_?W]#Y)="),
						Charset.forName("UTF-8"), fallback, replacement, true);
			}});
	}

	/**
	 * Get a new table instance based on the factory's current settings.
	 * 
	 * @param t
	 *            the type of table to return, this will override the factory's
	 *            default table type.
	 * @return returns a new table instance.
	 */
	public BrailleConverter newTable(TableType t) {
		return tables.get(t).newBrailleConverter();
	}

	//jvm1.6@Override
	public Collection<Table> list() {
		return tables.values();
	}

}
