<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output indent="yes" />
	
    <xsl:variable name="originalServiceName" select="'checkActiveAndPendingDevice'" />
	<xsl:variable name="originalSubService" select="'retrieveLastActiveDate'" />
	
  <xsl:variable name="customErrorMap">

<entry code="106">
			<errorCode>106</errorCode>
			<errorMsg>DEVICE NOT FOUND</errorMsg>
			<cicsErrorCode>106</cicsErrorCode>
</entry>

	</xsl:variable>
	<xsl:variable name="iterateForSuccessFlag" select="'false'" />
	<xsl:variable name="showWarningFlag" select="'false'" />
	<xsl:variable name="showSrvcResponseFlag" select="'true'" />
	<xsl:include href="FacadeShotGunServiceHeaderResponse.xsl" />

	<xsl:variable name="billingSys" select="/service/serviceHeader/billingSys"/>

	<xsl:template match="serviceResponse">
		<xsl:choose>	
			<xsl:when test="/service/serviceHeader/billingSys = 'VISION'">
				<xsl:for-each
					select="/service/serviceBody/serviceResponse/customerList/customer">
						<xsl:if test="visionBillingSystem != 'VISION_B2B'">
							<serviceResponse>
						<xsl:attribute name="billingSys">
							<xsl:value-of select="visionBillingSystem" />
						</xsl:attribute>
						<xsl:choose>
							<xsl:when test="'' != /service/serviceBody/serviceResponse/customerList/customer/id">
								<customerId><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/id" /></customerId>
								<accountNo><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/number" /></accountNo>
								<commonErrors>
									<serviceComponent></serviceComponent>
								</commonErrors>
								<deviceDetailsList>
									<deviceDetailsLineItem>
										<lastActiveDate><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/lineList/line/device/deviceActivationDate" /></lastActiveDate>
										<activeAtSwitchIndicator><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/lineList/line/device/simCardDisconnectIndicator" /></activeAtSwitchIndicator>
										<mtn><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/lineList/line/mdn" /></mtn>
									</deviceDetailsLineItem>
								</deviceDetailsList>
							</xsl:when>
							<xsl:otherwise>
								<commonErrors>
									<serviceComponent>PCST046N</serviceComponent>
								</commonErrors>
							</xsl:otherwise>
						</xsl:choose>
					</serviceResponse>
						</xsl:if> 
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="billingSys" select="/service/serviceHeader/billingSys" />
				<xsl:for-each
					select="/service/serviceBody/serviceResponse/customerList/customer[visionBillingSystem = $billingSys]">
					<xsl:variable name="numberOfOrders"
						select="count(accountList/account/serviceOrderList)" />
					<serviceResponse>
						<xsl:choose>
							<xsl:when test="'' != /service/serviceBody/serviceResponse/customerList/customer/id">
								<customerId><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/id" /></customerId>
								<accountNo><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/number" /></accountNo>
								<commonErrors>
									<serviceComponent></serviceComponent>
								</commonErrors>
								<deviceDetailsList>
									<deviceDetailsLineItem>
										<lastActiveDate><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/lineList/line/device/deviceActivationDate" /></lastActiveDate>
										<activeAtSwitchIndicator><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/lineList/line/device/simCardDisconnectIndicator" /></activeAtSwitchIndicator>
										<mtn><xsl:value-of select="/service/serviceBody/serviceResponse/customerList/customer/accountList/account/lineList/line/mdn" /></mtn>
									</deviceDetailsLineItem>
								</deviceDetailsList>
							</xsl:when>
							<xsl:otherwise>
								<commonErrors>
								<serviceComponent>PCST046N</serviceComponent>
								</commonErrors>
							</xsl:otherwise>
						</xsl:choose>
					</serviceResponse>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>