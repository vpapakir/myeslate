package gr.cti.eslate.base.container;

import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


public class PasswordDialog extends JDialog {
    public static final int LOCK_MODE = 1;
    public static final int UNLOCK_MODE = 0;
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;
    public static final int H_GAP = 5;
    public static final int V_GAP = 5;
    public static final int TEXT_FIELD_WIDTH = 220;
//    String passwd, passwd2;
    JLabel passLb, pass2Lb;
    JPasswordField passFld, pass2Fld;
    JButton okButton, cancelButton;
    ResourceBundle bundle;
    int returnCode = DIALOG_CANCELLED;
    int mode = UNLOCK_MODE;

    public PasswordDialog(ESlateContainer container, JDialog parentDialog, String password, String password2, boolean editEnabled, int mode) {
        super(parentDialog, true);
        this.mode = mode;
        initDialog(container, parentDialog, password, password2, editEnabled);
    }

    public PasswordDialog(ESlateContainer container, JFrame parentFrame, String password, String password2, boolean editEnabled, int mode) {
        super(parentFrame, true);
        this.mode = mode;
        initDialog(container, parentFrame, password, password2, editEnabled);
    }

    void initDialog(final ESlateContainer container, Component owner, String password, String password2, boolean editEnabled) {
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.PasswordDialogBundle", Locale.getDefault());
        String passLbText = null;
        if (mode == LOCK_MODE) {
            setTitle(bundle.getString("ProtectTitle"));
            passLbText = bundle.getString("EnterPassword");
        }else{
            setTitle(bundle.getString("UnprotectTitle"));
            passLbText = bundle.getString("Password");
        }

        JPanel mainPanel = new JPanel();
        ExplicitLayout el = new ExplicitLayout();
        mainPanel.setLayout(el);

        passLb = new JLabel(passLbText);
        pass2Lb = new JLabel(bundle.getString("ConfirmPassword"));

        passFld = new JPasswordField();
        pass2Fld = new JPasswordField();
        Font f = new Font("Monospaced", Font.PLAIN, 12);
        passFld.setFont(f);
        pass2Fld.setFont(f);

        okButton = new JButton(bundle.getString("OK"));
        cancelButton = new JButton(bundle.getString("Cancel"));
        okButton.setForeground(ESlateContainerUtils.color128);
        cancelButton.setForeground(ESlateContainerUtils.color128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        okButton.setMargin(zeroInsets);
        cancelButton.setMargin(zeroInsets);


        int fieldHeight = passLb.getPreferredSize().height;
        if (passFld.getPreferredSize().height > fieldHeight)
            fieldHeight = passFld.getPreferredSize().height;

        Expression fieldHeightExp = MathEF.constant(fieldHeight);
        Expression vgapExp = MathEF.constant(V_GAP);
        Expression hgapExp = MathEF.constant(H_GAP);

        ExplicitConstraints ec1 = new ExplicitConstraints(passLb);
        ec1.setX(ContainerEF.left(mainPanel));
        ec1.setY(ContainerEF.top(mainPanel));
        ec1.setHeight(fieldHeightExp);

        ExplicitConstraints ec2 = new ExplicitConstraints(pass2Lb);
        ec2.setX(ContainerEF.left(mainPanel));
        ec2.setY(ComponentEF.bottom(passLb).add(V_GAP));
        ec2.setHeight(fieldHeightExp);

        Component[] labels = new Component[] {passLb, pass2Lb};
        Expression secColumnXPos = null;
        if (mode == LOCK_MODE) {
//            Expression maxLabelWidth  =  MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), labels));
            Expression maxLabelWidth  =  GroupEF.preferredWidthMax(labels);
            secColumnXPos = ContainerEF.left(mainPanel).add(maxLabelWidth).add(H_GAP);
        }else
            secColumnXPos = ComponentEF.right(passLb).add(H_GAP);

        ExplicitConstraints ec4 = new ExplicitConstraints(passFld);
        Expression fieldWidthExp = MathEF.constant(TEXT_FIELD_WIDTH);
        ec4.setX(secColumnXPos);
        ec4.setY(ComponentEF.top(passLb));
        ec4.setWidth(ContainerEF.width(mainPanel).subtract(secColumnXPos).add(ContainerEF.left(mainPanel)));
        ec4.setHeight(fieldHeightExp);
        ec4.setHeightZeroIfInvisible(true);
        ec4.setWidthZeroIfInvisible(true);

        ExplicitConstraints ec5 = new ExplicitConstraints(pass2Fld);
        ec5.setX(secColumnXPos);
        ec5.setY(ComponentEF.top(pass2Lb));
        ec5.setWidth(ContainerEF.width(mainPanel).subtract(secColumnXPos).add(ContainerEF.left(mainPanel)));
        ec5.setHeight(fieldHeightExp);
        ec5.setHeightZeroIfInvisible(true);
        ec5.setWidthZeroIfInvisible(true);


        Expression panelHeightExp = fieldHeightExp;
        if (mode == LOCK_MODE) {
            panelHeightExp = panelHeightExp.multiply(2);
            panelHeightExp = panelHeightExp.add(V_GAP);
        }
        panelHeightExp = panelHeightExp.add(V_GAP);
        Expression panelWidthExp = secColumnXPos.add(fieldWidthExp);
        el.setPreferredLayoutSize(panelWidthExp, panelHeightExp);

        mainPanel.add(passLb, ec1);
        mainPanel.add(pass2Lb, ec2);
        mainPanel.add(passFld, ec4);
        mainPanel.add(pass2Fld, ec5);

        TitledBorder tb1 = new TitledBorder(bundle.getString("ProtectMicroworld"));
        tb1.setTitleColor(ESlateContainerUtils.titleBorderColor);
        mainPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(5,5,0,5)));

        JPanel buttonPanel = new JPanel();
        ExplicitLayout el1 = new ExplicitLayout();
        buttonPanel.setLayout(el1);

        Component[] buttons = new Component[] {okButton, cancelButton};
        Expression butonsWidth = GroupEF.widthSum(buttons);

        Expression buttonWidthExp = MathEF.constant(ESlateContainerUtils.buttonSize.width);
        Expression buttonHeightExp = MathEF.constant(ESlateContainerUtils.buttonSize.height);

        ExplicitConstraints ec21 = new ExplicitConstraints(okButton);
        ec21.setHeight(buttonHeightExp);
        ec21.setWidth(buttonWidthExp);
        ec21.setY(ContainerEF.top(buttonPanel));

        ec21.setX(ContainerEF.width(buttonPanel).subtract(butonsWidth).subtract(H_GAP).divide(2));

        ExplicitConstraints ec22 = new ExplicitConstraints(cancelButton);
        ec22.setHeight(buttonHeightExp);
        ec22.setWidth(buttonWidthExp);
        ec22.setX(ComponentEF.right(okButton).add(H_GAP));
        ec22.setY(ComponentEF.top(okButton));

        buttonPanel.add(okButton, ec21);
        buttonPanel.add(cancelButton, ec22);

        el1.setPreferredLayoutSize(ContainerEF.width(buttonPanel.getParent()), buttonHeightExp);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(mainPanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(5));

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                container.containerUtils.discardDialogButtonListeners(PasswordDialog.this);
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                dispose();
                container.containerUtils.discardDialogButtonListeners(PasswordDialog.this);
            }
        });

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
//                setOKButtonStatus();
            }
            public void removeUpdate(DocumentEvent e) {
//                setOKButtonStatus();
            }
            public void changedUpdate(DocumentEvent e) {
//                setOKButtonStatus();
            }

            void setOKButtonStatus() {
                String p1 = getPassword();
                if (p1 == null || p1.trim().length() == 0)
                    okButton.setEnabled(false);
                else{
                    if (mode == PasswordDialog.LOCK_MODE) {
                        String p2 = getPassword2();
                        if (p2 == null || p2.trim().length() == 0)
                            okButton.setEnabled(false);
                        else
                            okButton.setEnabled(true);
                    }else
                        okButton.setEnabled(true);
                }
            }

        };
        passFld.getDocument().addDocumentListener(dl);
        pass2Fld.getDocument().addDocumentListener(dl);

        // ESCAPE HANDLER
        container.containerUtils.attachDialogButtonListener(this, cancelButton, java.awt.event.KeyEvent.VK_ESCAPE);
        // ENTER HANDLER
        container.containerUtils.attachDialogButtonListener(this, okButton, java.awt.event.KeyEvent.VK_ENTER);

        // Initialization
        setPassword(password);
        setPassword2(password2);
        if (!editEnabled) {
            passFld.setEnabled(false);
            pass2Fld.setEnabled(false);
        }
        if (mode == UNLOCK_MODE) {
            pass2Lb.setVisible(false);
            pass2Fld.setVisible(false);
        }

        ESlateContainerUtils.showDialog(this, owner, true);
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getPassword() {
        String p = new String(passFld.getPassword());
        if (p != null && p.trim().length() == 0)
            p = null;
        return p;
    }

    public void setPassword(String password) {
        passFld.setText(password);
//        if (password == null || password.trim().length() == 0)
//            okButton.setEnabled(false);
    }

    public String getPassword2() {
        String p = new String(pass2Fld.getPassword());
        if (p != null && p.trim().length() == 0)
            p = null;
        return p;
    }

    public void setPassword2(String password) {
        pass2Fld.setText(password);
//        if (password == null || password.trim().length() == 0)
//            okButton.setEnabled(false);
    }

}
