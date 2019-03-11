<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output indent="yes" />
	<xsl:variable name="originalServiceName" select="'checkActiveAndPendingDevice'" />
	<xsl:variable name="originalSubService" select="'retrieveLastActiveDate'" />
	<xsl:variable name="customErrorMap">
	</xsl:variable>
	<xsl:variable name="iterateForSuccessFlag" select="'true'" />
	<xsl:variable name="showWarningFlag" select="'true'" />
	<xsl:variable name="showSrvcResponseElement" select="'false'" />
	<xsl:include href="FacadeShotGunServiceHeaderResponse.xsl" />
	<xsl:variable name="showSrvcResponseFlag" select="'false'" />

	<xsl:variable name="billingSys" select="/service/serviceHeader/billingSys"/>

	<xsl:template match="serviceResponse">
		<xsl:variable name="visionBillingSystem" select="/service/serviceBody/serviceResponse/customerList/customer"/>
		<xsl:choose>	
			<xsl:when test="/service/serviceHeader/billingSys = 'VISION'">
				<serviceResponse>
					<xsl:attribute name="billingSys"><xsl:value-of select="'VISION_NORTH'"/></xsl:attribute>
					<xsl:choose>
						<xsl:when test="/service/serviceBody/serviceResponse/customerList/customer/visionBillingSystem = 'VISION_NORTH'">
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
				<serviceResponse>
					<xsl:attribute name="billingSys"><xsl:value-of select="'VISION_WEST'"/></xsl:attribute>
					<xsl:choose>
						<xsl:when test="/service/serviceBody/serviceResponse/customerList/customer/visionBillingSystem = 'VISION_WEST'">
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
				<serviceResponse>
					<xsl:attribute name="billingSys"><xsl:value-of select="'VISION_EAST'"/></xsl:attribute>
					<xsl:choose>
						<xsl:when test="/service/serviceBody/serviceResponse/customerList/customer/visionBillingSystem = 'VISION_EAST'">
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
				<serviceResponse>
					<xsl:attribute name="billingSys"><xsl:value-of select="'VISION_B2B'"/></xsl:attribute>
					<xsl:choose>
						<xsl:when test="/service/serviceBody/serviceResponse/customerList/customer/visionBillingSystem = 'VISION_B2B'">
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
			</xsl:when>
			<xsl:otherwise>
				<serviceResponse>
					<xsl:choose>
						<xsl:when test="/service/serviceBody/serviceResponse/customerList/customer/visionBillingSystem">
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
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>