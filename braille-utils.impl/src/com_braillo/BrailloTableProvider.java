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
package com_braillo;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.daisy.braille.api.factory.FactoryProperties;
import org.daisy.braille.api.table.BrailleConverter;
import org.daisy.braille.api.table.Table;
import org.daisy.braille.api.table.TableProvider;
import org.daisy.braille.impl.table.EmbosserBrailleConverter;
import org.daisy.braille.impl.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.impl.table.EmbosserTable;

import aQute.bnd.annotation.component.Component;

@Component
public class BrailloTableProvider implements TableProvider {
	//public final static String IS_ONE_TO_ONE = "is one-to-one";
	//public final static String IS_DISPLAY_FORMAT = "is display format";
	
	enum TableType implements FactoryProperties {
		BRAILLO_6DOT_001_00("Braillo USA 6 DOT 001.00", "Compatible with Braillo USA 6 DOT 001.00"), 
				// US computer braille, compatible with
				// "Braillo USA 6 DOT 001.00"
		BRAILLO_6DOT_044_00("Braillo ENGLAND 6 DOT 044.00", "Compatible with Braillo ENGLAND 6 DOT 044.00"), 
				// US computer braille (lower case), compatible with
				// "Braillo ENGLAND 6 DOT 044.00" which is identical to
				// "Braillo USA 6 DOT 001.00"
		BRAILLO_6DOT_046_01("Braillo SWEDEN 6 DOT 046.01", "Compatible with Braillo SWEDEN 6 DOT 046.01"), 
				// compatible with "Braillo SWEDEN 6 DOT 046.01"
		BRAILLO_6DOT_047_01("Braillo NORWAY 6 DOT 047.01", "Compatible with Braillo NORWAY 6 DOT 047.01")
				// compatible with "Braillo NORWAY 6 DOT 047.01"
		;
		private final String name;
		private final String desc;
		private final String identifier;
		TableType(String name, String desc) {
			this.name = name;
			this.desc = desc;
			this.identifier = this.getClass().getCanonicalName() + "." + this.toString();
		}
		@Override
		public String getIdentifier() {
			return identifier;
		}
		@Override
		public String getDisplayName() {
			return name;
		}
		@Override
		public String getDescription() {
			return desc;
		}
	};

	private final Map<String, FactoryProperties> tables;

	public BrailloTableProvider() {
		tables = new HashMap<String, FactoryProperties>(); 
		addTable(TableType.BRAILLO_6DOT_001_00);
		addTable(TableType.BRAILLO_6DOT_044_00);		
		addTable(TableType.BRAILLO_6DOT_046_01);
		addTable(TableType.BRAILLO_6DOT_047_01);		
	}
	
	private void addTable(FactoryProperties t) {
		tables.put(t.getIdentifier(), t);
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
		return newFactory(t.getIdentifier()).newBrailleConverter();
	}

	public Table newFactory(String identifier) {
		FactoryProperties fp = tables.get(identifier);
		switch ((TableType)fp) {
		case BRAILLO_6DOT_001_00:
			return new EmbosserTable(TableType.BRAILLO_6DOT_001_00, EightDotFallbackMethod.values()[0], '\u2800'){

				/**
				 * 
				 */
				private static final long serialVersionUID = -3728256382860405787L;

				@Override
				public BrailleConverter newBrailleConverter() {
					return new EmbosserBrailleConverter(
							new String(
									" A1B'K2L@CIF/MSP\"E3H9O6R^DJG>NTQ,*5<-U8V.%[$+X!&;:4\\0Z7(_?W]#Y)="),
							Charset.forName("UTF-8"), fallback, replacement, true);
				}};
		case BRAILLO_6DOT_044_00:
			return new EmbosserTable(TableType.BRAILLO_6DOT_044_00, EightDotFallbackMethod.values()[0], '\u2800'){

				/**
				 * 
				 */
				private static final long serialVersionUID = 1636005108305141651L;

				@Override
				public BrailleConverter newBrailleConverter() {
					return new EmbosserBrailleConverter(
							new String(
									" a1b'k2l@cif/msp\"e3h9o6r^djg>ntq,*5<-u8v.%[$+x!&;:4\\0z7(_?w]#y)="),
							Charset.forName("UTF-8"), fallback, replacement, true);
				}};
		case BRAILLO_6DOT_046_01:
			return new EmbosserTable(TableType.BRAILLO_6DOT_046_01, EightDotFallbackMethod.values()[0], '\u2800'){
				 /**
				 * 
				 */
				private static final long serialVersionUID = -5911455102023772974L;

				// sv-SE
				@Override
				public BrailleConverter newBrailleConverter() {
					return new EmbosserBrailleConverter(
							new String(
									" a,b'k;l^cif/msp!e:h*o+r\"djg[ntq_1?2-u<v%396]x\\&#5.8>z=($4w70y)@"),
							Charset.forName("UTF-8"), fallback, replacement, true);
				}};
		case BRAILLO_6DOT_047_01:
			return new EmbosserTable(TableType.BRAILLO_6DOT_047_01, EightDotFallbackMethod.values()[0], '\u2800'){
				/**
				 * 
				 */
				private static final long serialVersionUID = -8132115348497209954L;

				// no-NO
				@Override
				public BrailleConverter newBrailleConverter() {
					return new EmbosserBrailleConverter(
							new String(
									" A,B.K;L`CIF/MSP'E:H@O!RaDJG[NTQ*]?r-U\"Vqm\\h&Xli_e%u$Z=k|dWg#Ynj"),
							Charset.forName("UTF-8"), fallback, replacement, false);
				}};
		default: return null;
		}
	}

	@Override
	public Collection<FactoryProperties> list() {
		return Collections.unmodifiableCollection(tables.values());
	}

}
