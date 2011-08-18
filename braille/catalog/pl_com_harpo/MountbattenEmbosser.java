package pl_com_harpo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.io.IOException;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.table.TableFilter;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.Table;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Area;
import org.daisy.paper.PrintPage;
import org.daisy.paper.Dimensions;
import org.daisy.printing.Device;

import pl_com_harpo.HarpoEmbosserProvider.EmbosserType;

import org.daisy.braille.embosser.EmbosserFactoryException;

public class MountbattenEmbosser extends AbstractEmbosser {

    private double maxPaperWidth = 330d;
    private double maxPaperHeight = 382d;
    private double minPaperWidth = 100d;
    private double minPaperHeight = 98d;

    private final static TableFilter tableFilter;
    private final static String table6dot = "org_daisy.EmbosserTableProvider.TableType.NABCC";

    static {
        tableFilter = new TableFilter() {
            //jvm1.6@Override
            public boolean accept(Table object) {
                if (object == null) { return false; }
                if (object.getIdentifier().equals(table6dot))     { return true; }
                return false;
            }
        };
    }

    public TableFilter getTableFilter() {
        return tableFilter;
    }

    public MountbattenEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        setTable = TableCatalog.newInstance().get(table6dot);

        setCellWidth(5.9d);
        setCellHeight(10.1d);
    }

    public boolean supportsPrintPage(Dimensions dim) {

        if (dim==null) { return false; }

        return (dim.getWidth()  <= maxPaperWidth)  &&
               (dim.getWidth()  >= minPaperWidth)  &&
               (dim.getHeight() <= maxPaperHeight) &&
               (dim.getHeight() >= minPaperHeight);
    }

    public boolean supportsVolumes() {
        return false;
    }

    public boolean supportsAligning() {
        return true;
    }

    public boolean supports8dot() {
        return false;
    }

    public boolean supportsDuplex() {
        return false;
    }

    public EmbosserWriter newEmbosserWriter(Device device) {

        try {
            File f = File.createTempFile(this.getClass().getCanonicalName(), ".tmp");
            f.deleteOnExit();
            EmbosserWriter ew = newEmbosserWriter(new FileOutputStream(f));
            return new FileToDeviceEmbosserWriter(ew, f, device);
        } catch (IOException e) {
        }
        throw new IllegalArgumentException("Embosser does not support this feature.");
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        PageFormat page = getPageFormat();

        if (!supportsPageFormat(page)) {
            throw new IllegalArgumentException(new EmbosserFactoryException("Unsupported paper"));
        }

        byte[] header = getMountbattenHeader();
        byte[] footer = getMountbattenFooter();

        EmbosserWriterProperties props =
            new SimpleEmbosserProperties(getMaxWidth(page), getMaxHeight(page))
                .supports8dot(supports8dot())
                .supportsDuplex(supportsDuplex())
                .supportsAligning(supportsAligning());

        return new ConfigurableEmbosser.Builder(os, setTable.newBrailleConverter())
                        .breaks(new MountbattenLineBreaks())
                        .padNewline(ConfigurableEmbosser.Padding.NONE)
                        .footer(footer)
                        .embosserProperties(props)
                        .header(header)
                        .build();
    }

    private byte[] getMountbattenHeader() {

        // Instruct users to
        // * (save settings)
        // * switch Mountbatten to the required state before printing (embossing mode, paper settings)
        // * (restore settings afterwards)

        // PageFormat page = getPageFormat();
        // double paperLenght = page.getHeight();

        StringBuffer header = new StringBuffer();

        // header.append("{std}");                 // Factory settings
        // header.append("{adv}");                 // Advanced mode
        // header.append("{m}");                   // Embossing speed
        // header.append("{cp}");                  // Continuous paper ?
        // header.append("{inch on}");
        // header.append("{fl x.y}");              // Form length in inch

        header.append("{man}");                 // Manual new line
        header.append("{bc 1}");                // Braille table = NABCC
        header.append("{lm 0}");                // Left margin = 0
        header.append("{rm 0}");                // Right margin = 0
        header.append("{tm 0}");                // Top margin = 0

        return header.toString().getBytes();
    }

    private byte[] getMountbattenFooter() {

        StringBuffer footer = new StringBuffer();

        // footer.append("{restore}");                 // Restore the default settings

        return footer.toString().getBytes();
    }

    @Override
    public Area getPrintableArea(PageFormat pageFormat) {

        PrintPage printPage = getPrintPage(pageFormat);

        double unprintableInner = 15d;
        double unprintableTop = 15d;
        double unprintableOuter = 15d;
        double unprintableBottom = 15d;
        double printablePageWidth = printPage.getWidth() - unprintableInner - unprintableOuter;
        double printablePageHeight = printPage.getHeight() - unprintableTop - unprintableBottom;

        return new Area(printablePageWidth,
                        printablePageHeight,
                        unprintableInner,
                        unprintableTop);
    }
}
