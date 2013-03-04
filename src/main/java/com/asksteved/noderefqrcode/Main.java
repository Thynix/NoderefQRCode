package com.asksteved.noderefqrcode;

import com.asksteved.noderefqrcode.webui.AddByQR;
import com.asksteved.noderefqrcode.webui.L10n;
import com.asksteved.noderefqrcode.webui.RenderQR;

import freenet.client.HighLevelSimpleClient;
import freenet.clients.http.Toadlet;
import freenet.clients.http.ToadletContainer;
import freenet.l10n.PluginL10n;
import freenet.pluginmanager.*;

public class Main implements FredPlugin, FredPluginThreadless, FredPluginVersioned {
	private PluginRespirator pr;
	private RenderQR renderQR;
	private AddByQR addByQR;

	static final String category = "QRCode";

	//
	// FredPluginVersioned
	//
	public String getVersion() {
		return "0.0.1";
	}

	//
	// FredPlugin
	//

	@Override
	public void terminate() {
		ToadletContainer container = pr.getToadletContainer();

		container.unregister(renderQR);
		container.unregister(addByQR);
	}

	@Override
	public void runPlugin(PluginRespirator pr) {
		this.pr = pr;
		ToadletContainer container = pr.getToadletContainer();
		HighLevelSimpleClient client = pr.getHLSimpleClient();

		L10n l10n = new L10n();

		renderQR = new RenderQR(client, pr.getNode());
		addByQR = new AddByQR(client, l10n, pr);

		container.register(renderQR, null, renderQR.path(), true, false);
		container.register(addByQR, "FProxyToadlet.categoryFriends", addByQR.path(), true, "AddPage", "AddPage", false, addByQR, l10n);
	}
}
