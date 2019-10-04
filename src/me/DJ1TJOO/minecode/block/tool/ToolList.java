package me.DJ1TJOO.minecode.block.tool;

import java.util.List;

public class ToolList {

	Tool[] tools;

	public ToolList(Tool... tools) {
		this.tools = tools;
	}

	public ToolList(List<String> tools) {
		Tool[] tools2 = new Tool[tools.size()];
		int i = 0;
		for (String string : tools) {
			tools2[i] = Tool.valueOf(string.toUpperCase());
			i++;
		}
		this.tools = tools2;
	}

	public Tool[] getTools() {
		return tools;
	}

	public void setTools(Tool[] tools) {
		this.tools = tools;
	}

	public Boolean contains(Tool tool) {
		for (Tool tools : tools) {
			if (tools.equals(tool)) {
				return true;
			}
		}
		return false;
	}

}
