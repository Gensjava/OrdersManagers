package ua.com.it_st.ordersmanagers.interfaces.implems;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ua.com.it_st.ordersmanagers.interfaces.DocBasementAction;
import ua.com.it_st.ordersmanagers.models.Pays;


public class PayDocBasementAction implements DocBasementAction {

    private Pays pays;

    public PayDocBasementAction(Pays pays) {
        this.pays = pays;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public double sum() {
        double total_nat = 0;
        double total_usd = 0;

        for (final Pays.PaysLines paysLines : pays.getPaysLines()) {
            total_nat = total_nat + paysLines.getSum_nat();
            total_usd = total_usd + paysLines.getSum_usd();
        }
        total_nat = new BigDecimal(total_nat).setScale(2, RoundingMode.UP).doubleValue();
        total_usd = new BigDecimal(total_usd).setScale(2, RoundingMode.UP).doubleValue();

        pays.setTotal_nut(total_nat);
        pays.setTotal_usd(total_usd);
        return total_usd;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
