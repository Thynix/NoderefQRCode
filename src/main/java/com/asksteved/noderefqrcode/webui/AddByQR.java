package com.asksteved.noderefqrcode.webui;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.*;
import freenet.pluginmanager.PluginRespirator;
import freenet.support.HTMLNode;
import freenet.support.api.HTTPRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Displays the local noderef as a QR code, and offers to parse an image of a QR code.
 */
public class AddByQR extends Toadlet implements LinkEnabledCallback {
	private final L10n l10n;
	private final PluginRespirator pr;

	public static final String PATH = "/qr/addfriend";

	public AddByQR(HighLevelSimpleClient client, L10n l10n, PluginRespirator pr) {
		super(client);
		this.l10n = l10n;
		this.pr = pr;
	}

	@Override
	public String path() {
		return PATH;
	}

	public void handleMethodGET(URI uri, HTTPRequest request, ToadletContext ctx)
	    throws ToadletContextClosedException, IOException
	{
		PageMaker pm = ctx.getPageMaker();
		PageNode pn = pm.getPageNode(l10n.getString("AddPage"), ctx);
		HTMLNode content = pn.content;
		content.addChild("img",
		    new String[] { "width", "height", "src"},
		    new String[] { Integer.toString(RenderQR.dimension), Integer.toString(RenderQR.dimension), RenderQR.PATH });

		HTMLNode form = ctx.addFormChild(content, PATH, "upload-code");
		form.addChild("input",
		    new String[] { "type", "name", "value"},
		    new String[] { "file", "file", ""});
		form.addChild("input",
		    new String[] { "type", "name" },
		    new String[] { "submit", "submit" });

		writeHTMLReply(ctx, 200, "OK", null, pn.outer.generate());
	}

	public void handleMethodPOST(URI uri, HTTPRequest request, ToadletContext ctx)
	    throws ToadletContextClosedException, IOException
	{
		InputStream stream = request.getPart("file").getInputStream();
		if (stream == null) {
			writeTextReply(ctx, 500, "Empty file.", "Uploaded file was empty.");
			return;
		}

		PageMaker pm = ctx.getPageMaker();
		PageNode pn = pm.getPageNode(l10n.getString("AddPage"), ctx);
		try {
			QRCodeReader qrCodeReader = new QRCodeReader();
			// See com.google.zxing.client.j2se.DecodeThread.decode
			BufferedImage image = ImageIO.read(stream);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			writeTextReply(ctx, 200, "QR code decoding successful.", qrCodeReader.decode(bitmap).getText());
		} catch (ReaderException e) {
			pn.content.setContent("Unable to read QR code:" + e);
			writeHTMLReply(ctx, 500, "QR code parsing error", pn.outer.generate());
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <br>Show menu entry:</br>
	 * <ul>
	 * <li>When not in public gateway mode.</li>
	 * <li>When allowed full access.</li>
	 * </ul>
	 */
	@Override
	public boolean isEnabled(ToadletContext ctx) {
		return (!pr.getToadletContainer().publicGatewayMode()) ||
		    ((ctx != null) && ctx.isAllowedFullAccess());
	}
}
