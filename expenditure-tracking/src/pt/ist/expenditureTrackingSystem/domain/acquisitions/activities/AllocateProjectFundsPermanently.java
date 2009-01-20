package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class AllocateProjectFundsPermanently<T extends PaymentProcess> extends AbstractActivity<T> {

    @Override
    protected boolean isAccessible(final T process) {
	return process.isProjectAccountingEmployee();
    }

    @Override
    protected boolean isAvailable(final T process) {
	return isCurrentUserProcessOwner(process)
		&& process.isInvoiceConfirmed()
		&& allItemsAreFilledWithRealValues(process)
		&& process.getRequest().isEveryItemFullyAttributeInRealValues()
		&& !process.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    private boolean allItemsAreFilledWithRealValues(final T process) {
	for (final RequestItem requestItem : process.getRequest().getRequestItemsSet()) {
	    if (!requestItem.isFilledWithRealValues()) {
		return false;
	    }
	}
	return true;
    }

    @Override
    protected void process(final T process, final Object... objects) {
	if (!process.isRealValueEqualOrLessThanFundAllocation()) {
	    throw new DomainException("activities.message.exception.valuesCannotGoOverFundAllocation");
	}
	final List<FundAllocationBean> fundAllocationBeans = (List<FundAllocationBean>) objects[0];
	for (FundAllocationBean fundAllocationBean : fundAllocationBeans) {
	    final ProjectFinancer projectFinancer = (ProjectFinancer) fundAllocationBean.getFinancer();
	    projectFinancer.addEffectiveProjectFundAllocationId(fundAllocationBean.getEffectiveFundAllocationId());
	}
    }

}
