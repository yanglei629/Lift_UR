package com.yanglei.LIFT.impl;

import java.awt.*;

public class V3Style extends Style {
	private static final int HORIZONTAL_SPACING = 7;
	private static final int VERTICAL_SPACING = 15;
	private static final int HORIZONTAL_INDENT = 14;
	private static final Dimension INPUTFIELD_SIZE = new Dimension(200, 24);
	private static final Dimension INPUTSHORTFIELD_SIZE = new Dimension(60, 24);
	@Override
	public Dimension getInputfieldSize()
	{
		return INPUTFIELD_SIZE;
	}
	@Override
	public Dimension getInputShortFieldSize()
	{
		return INPUTSHORTFIELD_SIZE;
	}
	@Override
	protected int getHorizontalSpacing()
	{
		return HORIZONTAL_SPACING;
	}
	@Override
	protected int getVertivcalSpacing()
	{
		return VERTICAL_SPACING;
	}
	@Override
	protected int getHorizontalIndent()
	{
		return HORIZONTAL_INDENT;
	}
}
