<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>


<bean:define id="itemID" name="item" property="externalId" type="java.lang.String"/>
<bean:define id="totalItems" name="allItems" />
<bean:define id="currentIndex" name="currentIndex"/>


	<tr>
		<th>Item</th>
		<th colspan="2">Descrição</th>
		<th></th>
		<th></th>
		<th name="operations"></th>
	</tr>
	<tr>
		<td rowspan="2">
			<%= currentIndex %>/<%= totalItems %>
		</td>
		<td rowspan="2" colspan="2" class="aleft">
			<p class="mvert0"><fr:view name="item" property="description"/></p>
			<ul>
				<li>
					<bean:message key="refundItem.label.salesCode" bundle="ACQUISITION_RESOURCES"/>:
					<fr:view name="item" property="CPVReference" >
						<fr:layout name="format">
							<fr:property name="format" value="${code} - ${description}"/>
						</fr:layout>
					</fr:view>
				</li>
				<logic:notEmpty name="item" property="unitItems">
					<li>
						<bean:message key="acquisitionProcess.label.payingUnits" bundle="ACQUISITION_RESOURCES"/>:<br/>
						<logic:iterate id="unitItem" name="item" property="sortedUnitItems">
							<fr:view name="unitItem" property="unit.presentationName"/>
							<logic:present name="unitItem" property="realShareValue">
								<fr:view name="unitItem" property="realShareValue"/>
							</logic:present>
							<logic:notPresent name="unitItem" property="realShareValue">
								<fr:view name="unitItem" property="shareValue"/>
							</logic:notPresent>
						</logic:iterate>
					</li>
				</logic:notEmpty>
			</ul>
		</td>
		
		<td class="nowrap aleft"><bean:message key="label.value" bundle="EXPENDITURE_RESOURCES"/>:</td>
		<td class="nowrap aright"><fr:view name="item" property="value"/></td>
	
		<td rowspan="2" class="nowrap aleft" name="operations">
			<ul style="padding-top: 0;">
				<wf:activityLink id="<%= "edit-" + itemID %>" processName="process" activityName="EditRefundItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="EditRefundItem" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id="<%= "assignPayingUnits-" + itemID %>" processName="process" activityName="GenericAssignPayingUnitToItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="GenericAssignPayingUnitToItem" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id="<%= "createRefundInvoice-" + itemID %>" processName="process" activityName="CreateRefundInvoice" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="CreateRefundInvoice" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id="<%= "distributeRealValuesForPayingUnits-" + itemID %>" processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request" paramName0="item" paramValue0="<%= itemID %>">
						<bean:define id="needsSeparator" value="true" toScope="request"/>
						<li><wf:activityName processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request"/></li>
				</wf:activityLink>

				<wf:activityLink id="<%= "deleteRefundItem-" + itemID %>" processName="process" activityName="DeleteRefundItem" scope="request" paramName0="item" paramValue0="<%= itemID %>">
					<bean:define id="needsSeparator" value="true" toScope="request"/>
					<li><wf:activityName processName="process" activityName="DeleteRefundItem" scope="request"/></li>
				</wf:activityLink>

			</ul>
			
			<script type="text/javascript">
				<bean:define id="hideOperations" value="true" toScope="request"/>
		
				<wf:isActive processName="process" activityName="EditRefundItem" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
				</wf:isActive>
				<wf:isActive processName="process" activityName="GenericAssignPayingUnitToItem" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
						</wf:isActive>
				<wf:isActive processName="process" activityName="CreateRefundInvoice" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
					</wf:isActive>
				<wf:isActive processName="process" activityName="DistributeRealValuesForPayingUnits" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
					</wf:isActive>
				<wf:isActive processName="process" activityName="DeleteRefundItem" scope="request">
					<bean:define id="hideOperations" value="false" toScope="request"/>
					</wf:isActive>
		
				<logic:equal name="hideOperations" value="true">
					$("[name='operations']").hide();
				</logic:equal>
			</script>
		</td>
	</tr>
	<tr>

		<td class="nowrap aleft"><bean:message key="label.refundValue" bundle="EXPENDITURE_RESOURCES"/>:</td>
		<td class="nowrap aright"><fr:view name="item" property="realValue" type="myorg.domain.util.Money" layout="null-as-label"/></td>
	</tr>
