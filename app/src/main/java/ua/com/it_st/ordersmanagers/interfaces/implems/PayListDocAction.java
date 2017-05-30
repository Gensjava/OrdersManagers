package ua.com.it_st.ordersmanagers.interfaces.implems;

import java.util.List;

import ua.com.it_st.ordersmanagers.interfaces.DocListAction;
import ua.com.it_st.ordersmanagers.models.Pays;
import ua.com.it_st.ordersmanagers.models.TableLines;

/**
 * Created by Gena on 2017-05-27.
 */

public class PayListDocAction implements DocListAction {

    private List<Pays.PaysLines> paysLines;

    public PayListDocAction(List<Pays.PaysLines> paysLines) {
        this.paysLines = paysLines;
    }

    @Override
    public boolean add(TableLines item) {
        paysLines.add((Pays.PaysLines) item);
        return false;
    }

    @Override
    public boolean update(TableLines item) {
        return false;
    }

    @Override
    public boolean delete(TableLines item) {
        paysLines.remove(item);
        return false;
    }

    @Override
    public boolean deleteAll() {
        paysLines.clear();
        return false;
    }
}
