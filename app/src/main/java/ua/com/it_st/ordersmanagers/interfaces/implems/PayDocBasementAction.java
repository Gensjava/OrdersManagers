package ua.com.it_st.ordersmanagers.interfaces.implems;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ua.com.it_st.ordersmanagers.interfaces.DocBasementAction;
import ua.com.it_st.ordersmanagers.models.Pays;

/**
 * Created by Gena on 2017-05-29.
 */

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
        double total = 0;

        for (final Pays.PaysLines paysLines : pays.getPaysLines()) {
            total = total + paysLines.getSum();
        }
        total = new BigDecimal(total).setScale(2, RoundingMode.UP).doubleValue();

        pays.setTotal(total);
        return total;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
