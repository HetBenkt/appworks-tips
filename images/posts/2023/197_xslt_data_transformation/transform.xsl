<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<output>
			<header>My CD Collection</header>
			<xsl:apply-templates select="catalog"/>
		</output>
	</xsl:template>
	<!-- for each CD in selected catalog apply this template! -->
	<xsl:template match="cd">
		<cd-info>
			<cd-title>
				<xsl:value-of select="title"/>
			</cd-title>
			<cd-artist>
				<xsl:value-of select="artist"/>
			</cd-artist>
		</cd-info>
	</xsl:template>
</xsl:stylesheet>