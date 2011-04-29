package com_viewplus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.table.AbstractConfigurableTableProvider;
import org.daisy.braille.table.AdvancedBrailleConverter;
import org.daisy.braille.table.BrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.table.EmbosserTable;
import org.daisy.braille.table.Table;
import org.daisy.braille.tools.StringTranslator.MatchMode;

/**
 *
 * @author Bert Frees
 */
public class ViewPlusTableProvider extends AbstractConfigurableTableProvider<ViewPlusTableProvider.TableType> {

    enum TableType { TIGER_INLINE_SUBSTITUTION_8DOT };

    private final ArrayList<Table> tables;

    public ViewPlusTableProvider() {
        super(EightDotFallbackMethod.values()[0], '\u2800');
        tables = new ArrayList<Table>();
        tables.add(new EmbosserTable<TableType>("Tiger inline substitution 8-dot", "", TableType.TIGER_INLINE_SUBSTITUTION_8DOT, this));
    }

    public BrailleConverter newTable(TableType t) {

        switch (t) {
            case TIGER_INLINE_SUBSTITUTION_8DOT:
                final String SUB = String.valueOf((char)0x1a);
                ArrayList<String> a = new ArrayList<String>();
                for (int i=0; i<256; i++) {
                    a.add(SUB + (char)i);
                }
                return new AdvancedBrailleConverter(
                    a.toArray(new String[a.size()]),
                    Charset.forName("ISO-8859-1"),
                    false,
                    MatchMode.RELUCTANT);
            default:
                throw new IllegalArgumentException("Cannot find table type " + t);
        }
    }

    //jvm1.6@Override
    public Collection<Table> list() {
        return tables;
    }
}
