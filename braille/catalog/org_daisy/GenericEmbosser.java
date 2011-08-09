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
package org_daisy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;
import org.daisy.paper.Dimensions;
import org.daisy.printing.Device;

public class GenericEmbosser extends AbstractEmbosser {
	private final static TableFilter tableFilter;
	
	static {
		tableFilter = new TableFilter() {
			//jvm1.6@Override
			public boolean accept(Table object) {
				if (object!=null) {
					return true; 
				} else { return false; }
			}
		};
	}
	
	public GenericEmbosser(String name, String desc,  Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
		setFeature(EmbosserFeatures.CELL_WIDTH, 6);
		setFeature(EmbosserFeatures.CELL_HEIGHT, 10);
	}

	//jvm1.6@Override
	public boolean supportsDimensions(Dimensions dim) {
		return true;
	}

	//jvm1.6@Override
	public TableFilter getTableFilter() {
		return tableFilter;
	}
	
	//jvm1.6@Override
	public EmbosserWriter newEmbosserWriter(OutputStream os) {
		if (!supportsDimensions(getPageFormat())) {
			throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
		}
		TableCatalog btb = TableCatalog.newInstance();
		Table tc = btb.get(setTable.getIdentifier());
		tc.setFeature("fallback", getFeature("fallback"));
		tc.setFeature("replacement", getFeature("replacement"));
		ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, tc.newBrailleConverter());
		b.breaks((String)getFeature("breaks"));
		b.padNewline((String)getFeature("padNewline"));
		final int maxWidth;
		final int maxHeight;
		if (getPageFormat()!=null) {
			maxWidth = EmbosserTools.getWidth(getPageFormat(), getCellWidth());
			maxHeight = EmbosserTools.getHeight(getPageFormat(), getCellHeight());
		} else {
			maxWidth = Integer.MAX_VALUE;
			maxHeight = Integer.MAX_VALUE;
		}
		b.embosserProperties(
				new SimpleEmbosserProperties(maxWidth, maxHeight)
				.supports8dot(true)
				.supportsDuplex(true)
				.supportsAligning(false));
		return b.build();
	}

	//jvm1.6@Override
	public EmbosserWriter newEmbosserWriter(Device device) {
		if (!supportsDimensions(getPageFormat())) {
			throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
		}
		try {
			File f = File.createTempFile(this.getClass().getCanonicalName(), ".tmp");
			f.deleteOnExit();
			EmbosserWriter ew = newEmbosserWriter(new FileOutputStream(f));
			return new FileToDeviceEmbosserWriter(ew, f, device);
		} catch (IOException e) {
			// do nothing, fail
		}
		throw new IllegalArgumentException("Embosser does not support this feature.");
	}

    public boolean supportsVolumes() {
        return false;
    }

    public boolean supports8dot() {
        return false;
    }

    public boolean supportsDuplex() {
        return true;
    }

    public boolean supportsAligning() {
        return false;
    }
}