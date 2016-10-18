package com.manikssys.in.reports.windows;

import com.manikssys.in.common.*;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;
import com.manikssys.in.security.beans.ScrUserMaster;
import org.apache.log4j.Logger;
import com.manikssys.in.common.ToolbarComposer;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.business.IPortMaster;
import com.manikssys.in.operational.business.PortMasterBs;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;

/**

 * User: sandeep
 * Date: Sep 27, 2010
 */
public class ReportSettingComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Window reportSettingWin;
    private Combobox cmbPosition;
    private Listbox seqenceListBox;
    private Textbox portTextbox;
    private Image chooseAllBtn, chooseBtn, removeBtn, removeAllBtn, topBtn, upBtn, downBtn, bottomBtn;
    private Div seqDiv;
    private static Logger log;
    private ScrUserMaster loggedUser;
    private ToolbarComposer toolBarComposer = null;
    private List<OprPortMaster> portList;
    private List<OprPortMaster> portSeqList;
    private IPortMaster iPortBs;
//    private ListModelList chosenDataModel;
    // EVENTS

    public void onCreate$reportSettingWin() {
        System.out.println("onCreate$reportSettingWin  clled....");
        try {
            Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
            log = Logger.getLogger(ReportComposer.class);
            loggedUser = (ScrUserMaster) sessionScope.get("user");
            toolBarComposer = (ToolbarComposer) Executions.getCurrent().getDesktop().getAttribute("commonHBox$composer");
            iPortBs = new PortMasterBs();
            clearScreeen();
            portSeqList = new ArrayList<OprPortMaster>();
            fillPositionCmb();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onChange$portTextbox() {
        try {
            ComponentValidation.removeErrorMessage(portTextbox);
            OprPortMaster port = null;
            String lovFile = Labels.getLabel("app.vp.portlov");
            Window w = (Window) Executions.createComponents(lovFile, null, null); //("WEB-INF"+File.separator+"presentation"+File.separator+"LOV"+File.separator+"CommonPortLov.zul", null, null);
            w.setAttribute("PARAM_TEXT", portTextbox.getText());
            w.setAttribute("OBJECT", null);
            w.setAttribute("width", "400px");
            w.setMaximizable(true);
            w.doModal();
            port = (OprPortMaster) w.getAttribute("OBJECT");
            if (port != null) {
                portTextbox.setText(port.getPortName());
                portTextbox.setAttribute("PORT", port);
            } else {
                portTextbox.setText("");
                portTextbox.setAttribute("PORT", null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void onChange$cmbPosition() {
        int index = cmbPosition.getSelectedIndex();
        if (index >= 0) {
            resetList();
            CommonOperation.beginTransaction();
            retreivePositionPort();
            CommonOperation.commitTransaction();
        }
    }

    private void resetList() {
        if (!seqenceListBox.getItems().isEmpty()) {
            seqenceListBox.getItems().clear();
        }
    }

    private void retreivePositionPort() {
        portSeqList.clear();
        seqDiv.setVisible(true);
        int position = 0;
        OprPortMaster example = new OprPortMaster();
        example.setStatus(Constants.NON_DELETED);
        example.setDeSeq(position);
        example.setLeSeq(position);
        example.setDwSeq(position);
        example.setLwSeq(position);
        position = Integer.parseInt(cmbPosition.getSelectedItem().getValue().toString());        
        switch (position) {
            case 3:
                example.setDeSeq(position);
                CommonOperation.beginTransaction();
                portList = iPortBs.fetchPortSeqList(example);
                CommonOperation.commitTransaction();
                for (OprPortMaster bean : portList) {
                    if (bean.getDeSeq() < 100000) {
                        addListItem(bean);
                    }
                }
                break;

            case 4:
                example.setLeSeq(position);
                CommonOperation.beginTransaction();
                portList = iPortBs.fetchPortSeqList(example);
                CommonOperation.commitTransaction();
                for (OprPortMaster bean : portList) {
                    if (bean.getLeSeq() < 100000) {
                        addListItem(bean);
                    }
                }
                break;

            case 5:
                example.setDwSeq(position);
                CommonOperation.beginTransaction();
                portList = iPortBs.fetchPortSeqList(example);
                CommonOperation.commitTransaction();
                for (OprPortMaster bean : portList) {
                    if (bean.getDwSeq() < 100000) {
                        addListItem(bean);
                    }
                }
                break;

            case 6:
                example.setLwSeq(position);
                CommonOperation.beginTransaction();
                portList = iPortBs.fetchPortSeqList(example);
                CommonOperation.commitTransaction();
                for (OprPortMaster bean : portList) {
                    if (bean.getLwSeq() < 100000) {
                        addListItem(bean);
                    }
                }
                break;
        }
    }

    private void addListItem(OprPortMaster bean) {
        Listitem lstItem = new Listitem();
        lstItem.setStyle("border-bottom: 1px solid  #86A4BE; height : 20px;");
        lstItem.setValue(bean);
        Listcell lstCell1 = new Listcell();
        lstCell1.setLabel(bean.getPortCode());
        lstCell1.setParent(lstItem);
        Listcell lstCell2 = new Listcell();
        lstCell2.setLabel(bean.getPortName());
        lstCell2.setParent(lstItem);
        seqenceListBox.appendChild(lstItem);
    }

    private void fillPositionCmb() {
        Comboitem cmbItem = new Comboitem();
        cmbItem.setLabel(Labels.getLabel("app.report.title.vpeastdry"));
        cmbItem.setValue(3);
        cmbItem.setParent(cmbPosition);

        Comboitem cmbItem1 = new Comboitem();
        cmbItem1.setLabel(Labels.getLabel("app.report.title.vpeastliquid"));
        cmbItem1.setValue(4);
        cmbItem1.setParent(cmbPosition);

        Comboitem cmbItem2 = new Comboitem();
        cmbItem2.setLabel(Labels.getLabel("app.report.title.vpwestdry"));
        cmbItem2.setValue(5);
        cmbItem2.setParent(cmbPosition);

        Comboitem cmbItem3 = new Comboitem();
        cmbItem3.setLabel(Labels.getLabel("app.report.title.vpwestliquid"));
        cmbItem3.setValue(6);
        cmbItem3.setParent(cmbPosition);
    }

    public void onClick$topBtn() {
        top();
    }

    public void onClick$upBtn() {
        up();
    }

    public void onClick$downBtn() {
        down();
    }

    public void onClick$bottomBtn() {
        bottom();
    }

    // Button Action - Move to Top
    public void top() {
        if (!seqenceListBox.getItems().isEmpty()) {
            Listitem obj = seqenceListBox.getSelectedItem();
            seqenceListBox.getItems().remove(obj);
            seqenceListBox.getItems().add(0, obj);
        }
    }

    // Button Action - Move up
    public void up() {
        int index = seqenceListBox.getSelectedIndex();
        if (index == 0 || index < 0) //first one or nothing selected
        {
            return;
        }
        Listitem obj = seqenceListBox.getSelectedItem();
        seqenceListBox.getItems().remove(obj);
        seqenceListBox.getItems().add(--index, obj);
    }

    // Button Action - Move down
    public void down() {
        int index = seqenceListBox.getSelectedIndex();
        if (index == seqenceListBox.getItems().size() - 1 || index < 0) //last one or nothing selected
        {
            return;
        }
        Listitem obj = seqenceListBox.getSelectedItem();
        seqenceListBox.getItems().remove(obj);
        seqenceListBox.getItems().add(++index, obj);
    }

    // Button Action - Move to Bottom
    public void bottom() {
        if (!seqenceListBox.getItems().isEmpty()) {
            int lastPosition = seqenceListBox.getItems().size() - 1;
            Listitem obj = seqenceListBox.getSelectedItem();
            seqenceListBox.getItems().remove(obj);
            seqenceListBox.getItems().add(lastPosition, obj);
        }
    }

    public void clearScreeen() {
        seqenceListBox.getItems().clear();
        seqDiv.setVisible(false);
    }

    public void saveRecord() throws Exception {
        List<Listitem> seqListItems = seqenceListBox.getItems();
        int position = 1;
        int col = Integer.parseInt(cmbPosition.getSelectedItem().getValue().toString());
        CommonOperation.beginTransaction();
        for (Listitem item : seqListItems) {
            OprPortMaster port = (OprPortMaster) item.getValue();
            position++;
            switch (col) {
                case 3:
                    port.setDeSeq(position);
                    break;

                case 4:
                    port.setLeSeq(position);
                    break;

                case 5:
                    port.setDwSeq(position);
                    break;

                case 6:
                    port.setLwSeq(position);
                    break;
            }
            port = iPortBs.savePort(port);
        }
        CommonOperation.commitTransaction("label.opr.reportsetting.save");
    }

    public void addRecord() throws Exception {
        int index = seqenceListBox.getSelectedIndex();
        if (portTextbox.getAttribute("PORT") == null) {
            return;
        } else {
            OprPortMaster bean = (OprPortMaster) portTextbox.getAttribute("PORT");
            List<Listitem> seqListItems = seqenceListBox.getItems();
            int location = 1;
            for (Listitem item : seqListItems) {
                OprPortMaster port = (OprPortMaster) item.getValue();
                if (port.getPortId().equals(bean.getPortId())) {
                    Messagebox.show("Port Allready in Sequence List at location "+location);
                    return;
                }
                location++;
            }
            seqenceListBox.getItems().add(++index, getItem(bean));
        }
    }

    public void closeScreen() {
    }

    public void retrieveRecord() {

    }

    public void deleteRecord() throws Exception {
        Listitem item = seqenceListBox.getSelectedItem();
        OprPortMaster example = (OprPortMaster) item.getValue();
        if (example != null) {

            int position = 100000;

            int col = Integer.parseInt(cmbPosition.getSelectedItem().getValue().toString());
            System.out.println("Position ______________ " + position);
            switch (col) {
                case 3:
                    example.setDeSeq(position);
                    break;
                case 4:
                    example.setLeSeq(position);
                    break;
                case 5:
                    example.setDwSeq(position);
                    break;
                case 6:
                    example.setLwSeq(position);
                    break;
            }
            CommonOperation.beginTransaction();
            example = iPortBs.savePort(example);
            CommonOperation.commitTransaction("label.opr.reportsetting.delete");
            seqenceListBox.getItems().remove(item);
        }
    }

    public void setControls() {
    }

    public void getFirstRecord() {
    }

    public void getLastRecord() {
    }

    public void getNextRecord() {
    }

    public void getPreviousRecord() {
    }

    public Listitem getItem(OprPortMaster bean) {
        Listitem lstItem = new Listitem();
        lstItem.setValue(bean);
        Listcell lstCell1 = new Listcell();
        lstCell1.setLabel(bean.getPortCode());
        lstCell1.setParent(lstItem);
        Listcell lstCell2 = new Listcell();
        lstCell2.setLabel(bean.getPortName());
        lstCell2.setParent(lstItem);
        return lstItem;
    }
}
