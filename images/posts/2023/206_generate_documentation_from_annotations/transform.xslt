<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <head>
        <style>
          table {
            border-collapse: collapse;
            width: 100%;
          }
          th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
          }
          th {
            background-color: #f2f2f2;
          }
        </style>
      </head>
      <body>
        <table>
          <tr>
            <th>Name</th>
            <th>Internal Name</th>
            <th>Created By</th>
            <th>Created Date</th>
            <th>Last Modified By</th>
            <th>Last Modified Date</th>
            <th>Substring</th>
            <th>Access Control Policy ID</th>
          </tr>
          <xsl:apply-templates select="//xds_document"/>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="xds_document">
    <tr>
      <td><xsl:value-of select="name"/></td>
      <td><xsl:value-of select="internal_name"/></td>
      <td><xsl:value-of select="created_by"/></td>
      <td><xsl:value-of select="created_date"/></td>
      <td><xsl:value-of select="lastmodified_by"/></td>
      <td><xsl:value-of select="lastmodified_date"/></td>
      <td><xsl:value-of select="substring"/></td>
      <td><xsl:value-of select="accesscontrolpolicy_id"/></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>
