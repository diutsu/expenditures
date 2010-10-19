package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ConfirmInvoice extends WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user) && person != null && process.isActive() && !process.isInvoiceReceived()
		&& !process.getUnconfirmedInvoices(person).isEmpty() && process.isResponsibleForUnit(person);
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	activityInformation.getProcess().confirmInvoiceBy(UserView.getCurrentUser().getExpenditurePerson());
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isConfirmationNeeded(RegularAcquisitionProcess process) {
	User currentUser = UserView.getCurrentUser();
	Set<AcquisitionInvoice> unconfirmedInvoices = process.getUnconfirmedInvoices(currentUser.getExpenditurePerson());
	for (AcquisitionInvoice unconfirmedInvoice : unconfirmedInvoices) {
	    if (!StringUtils.isEmpty(unconfirmedInvoice.getConfirmationReport())) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public String getLocalizedConfirmationMessage(RegularAcquisitionProcess process) {
	StringBuilder builder = new StringBuilder();
	User currentUser = UserView.getCurrentUser();
	Set<AcquisitionInvoice> unconfirmedInvoices = process.getUnconfirmedInvoices(currentUser.getExpenditurePerson());
	for (AcquisitionInvoice unconfirmedInvoice : unconfirmedInvoices) {
	    builder.append(BundleUtil.getFormattedStringFromResourceBundle(getUsedBundle(), "activity.confirmation."
		    + getClass().getName(), unconfirmedInvoice.getInvoiceNumber(), unconfirmedInvoice.getConfirmationReport()));
	}

	return builder.toString();

    }
}
