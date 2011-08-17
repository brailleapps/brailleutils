package com_braillo;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.daisy.braille.embosser.UnsupportedWidthException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com_braillo.BrailloEmbosserProvider.EmbosserType;

public class Braillo440SW2PEmbosserTest extends AbstractTestBraillo440Embosser {

	public Braillo440SW2PEmbosserTest() {
		super(new Braillo440Embosser("Braillo 440SW (two page mode)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_440_SW_2P));
	}
	
	@Test
	public void testEmbossing() throws IOException, ParserConfigurationException, SAXException, UnsupportedWidthException, TransformerException {
		performTest("resource-files/test-input-1.pef", "resource-files/test-input-1_braillo440SW_2p_us_fa44-", ".prn", 3);
	}
}
