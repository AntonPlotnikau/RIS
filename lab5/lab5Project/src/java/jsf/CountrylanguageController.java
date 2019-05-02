package jsf;

import jpa.entities.Countrylanguage;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.CountrylanguageFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "countrylanguageController")
@SessionScoped
public class CountrylanguageController implements Serializable {

    private Countrylanguage current;
    private DataModel items = null;
    @EJB
    private jpa.session.CountrylanguageFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public CountrylanguageController() {
    }

    public Countrylanguage getSelected() {
        if (current == null) {
            current = new Countrylanguage();
            current.setCountrylanguagePK(new jpa.entities.CountrylanguagePK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private CountrylanguageFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Countrylanguage) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Countrylanguage();
        current.setCountrylanguagePK(new jpa.entities.CountrylanguagePK());
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.getCountrylanguagePK().setCountryCode(current.getCountry().getCode());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CountrylanguageCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Countrylanguage) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            current.getCountrylanguagePK().setCountryCode(current.getCountry().getCode());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CountrylanguageUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Countrylanguage) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CountrylanguageDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Countrylanguage.class)
    public static class CountrylanguageControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CountrylanguageController controller = (CountrylanguageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "countrylanguageController");
            return controller.ejbFacade.find(getKey(value));
        }

        jpa.entities.CountrylanguagePK getKey(String value) {
            jpa.entities.CountrylanguagePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new jpa.entities.CountrylanguagePK();
            key.setCountryCode(values[0]);
            key.setLanguage(values[1]);
            return key;
        }

        String getStringKey(jpa.entities.CountrylanguagePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getCountryCode());
            sb.append(SEPARATOR);
            sb.append(value.getLanguage());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Countrylanguage) {
                Countrylanguage o = (Countrylanguage) object;
                return getStringKey(o.getCountrylanguagePK());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Countrylanguage.class.getName());
            }
        }

    }

}
