package pt.ist.expenditureTrackingSystem.domain.statistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import myorg.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SimplifiedProcedureProcessStateTimeChartData extends PaymentProcessChartData {

    private List<Long>[] durations = null;

    public SimplifiedProcedureProcessStateTimeChartData(PaymentProcessYear paymentProcessYear) {
	super(paymentProcessYear);
	setTitle(BundleUtil.getStringFromResourceBundle("resources.AcquisitionResources", "label.process.state.times.median"));
    }

    private void init(final SimplifiedProcedureProcess simplifiedProcedureProcess) {
	if (durations == null) {
	    durations = durations = new List[AcquisitionProcessStateType.values().length];
	    for (int i = 0; i < durations.length; i++) {
		durations[i] = new ArrayList<Long>();
	    }
	}	
    }

    @Override
    public void calculateData() {
        super.calculateData();

        if (durations != null) {
            final BigDecimal DAYS_CONST = new BigDecimal(1000 * 3600 * 24);
            for (int i = 0; i < durations.length; i++) {
        	final List<Long> stateDurations = durations[i];
        	final int size = stateDurations == null ? 0 : stateDurations.size();
        	if (size > 0) { 
        	    final String key = AcquisitionProcessStateType.values()[i].getLocalizedName();

        	    Collections.sort(stateDurations);
        	    final double midPoint = 0.5 * size;
        	    final BigDecimal mediana;
        	    if (midPoint == (int) midPoint) {
        		final double nextPoint = midPoint - 1;
        		final Long value1 = stateDurations.get((int) midPoint);
        		final Long value2 = stateDurations.get((int) nextPoint);
        		final long sum = value1.longValue() + value2.longValue();
        		mediana = new BigDecimal(sum).divide(new BigDecimal(2));
        	    } else {
        		final double index = midPoint - 0.5;
        		mediana = new BigDecimal(stateDurations.get((int) index));
        	    }
        	    final BigDecimal value = mediana.divide(DAYS_CONST, 100, BigDecimal.ROUND_HALF_UP);
        	    registerData(key, value);
        	}
            }
        }
    }

    @Override
    protected void count(final PaymentProcess paymentProcess) {
	if (paymentProcess.isSimplifiedProcedureProcess()) {
	    final SimplifiedProcedureProcess simplifiedProcedureProcess = (SimplifiedProcedureProcess) paymentProcess;
	    init(simplifiedProcedureProcess);

	    final List<ProcessState> processStates = new ArrayList<ProcessState>(simplifiedProcedureProcess.getProcessStatesSet());
	    Collections.sort(processStates, ProcessState.COMPARATOR_BY_WHEN);
	    for (int i = 1; i < processStates.size(); i++) {
		final AcquisitionProcessState processState = (AcquisitionProcessState) processStates.get(i);
		final AcquisitionProcessState previousState = (AcquisitionProcessState) processStates.get(i - 1);
		final DateTime stateChangeDateTime = processState.getWhenDateTime();
		final DateTime previousStateChangeDateTime = previousState.getWhenDateTime();
		final long startMillis = stateChangeDateTime.getMillis();
		final long duration = startMillis - previousStateChangeDateTime.getMillis();

		final AcquisitionProcessStateType acquisitionProcessStateType = previousState.getAcquisitionProcessStateType();
		final int index = acquisitionProcessStateType.ordinal();

		durations[index].add(new Long(duration));
	    }
	}
    }

}
