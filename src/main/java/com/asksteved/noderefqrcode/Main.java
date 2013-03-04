package com.asksteved.noderefqrcode;

import com.asksteved.noderefqrcode.webui.RenderQR;

import freenet.clients.http.Toadlet;
import freenet.clients.http.ToadletContainer;
import freenet.pluginmanager.FredPlugin;
import freenet.pluginmanager.FredPluginThreadless;
import freenet.pluginmanager.FredPluginVersioned;
import freenet.pluginmanager.PluginRespirator;

public class Main implements FredPlugin, FredPluginThreadless, FredPluginVersioned {
	PluginRespirator pr;
	Toadlet renderQR;

	public String getVersion() {
		return "0.0.1";
	}

	@Override
	public void terminate() {
		ToadletContainer container = pr.getToadletContainer();
		container.unregister(renderQR);
	}

	@Override
	public void runPlugin(PluginRespirator pr) {
		this.pr = pr;
		ToadletContainer container = pr.getToadletContainer();

		renderQR = new RenderQR(pr.getHLSimpleClient(), pr.getNode());
		// "FProxyToadlet_categoryTitleFriends"
		container.register(renderQR, null, renderQR.path(), true, false);
	}

}
