package gr.cti.eslate.base.container;

import gr.cti.eslate.utils.ESlateOptionPane;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.zookitec.layout.ComponentEF;
import com.zookitec.layout.ContainerEF;
import com.zookitec.layout.ExplicitConstraints;
import com.zookitec.layout.ExplicitLayout;
import com.zookitec.layout.Expression;
import com.zookitec.layout.GroupEF;
import com.zookitec.layout.MathEF;


/** This is one of the nodes in the MicroworldProperties dialog. This panel contains
 *  information about the microworld, like its title, its authors. Also the microworld
 *  can be protected with a password, so that the microworld cannot be changed.
 */
class MetadataPanel extends JPanel {
    public static final int FIELD_HEIGHT = 20;
    public static final int V_GAP = 5;
    public static final int H_GAP = 4;
    MicroworldPropertiesDialog propertiesDialog;

    private String title = null, subject = null, company = null, category = null, comments = null;
    private String[] authors = null, keywords = null;
    private String password = null;

    JTextField titleFld, subjectFld, companyFld, keywordFld;
    JList authorsList;
    JComboBox categoryBox;
    JTextArea commentsArea;
    JLabel titleLabel, subjectLabel, companyLabel, categoryLabel, keywordLabel;
    JLabel commentLabel, authorLabel, lockLabel;
    JButton lockButton, remAuthorButton, addAuthorButton;

    Object[] categories;
    JScrollPane commentsScrollPane, authorScrollPane;
    ResourceBundle bundle;
    AuthorListModel authorListModel = new AuthorListModel();
    ESlateContainer container;
    /** This flag indicates whether the microworld had any password, when the MetadataPanel was
     * first displayed.
     */
//    boolean noMwdPassword = false;

    public MetadataPanel(MicroworldPropertiesDialog dialog) {
        super();
        propertiesDialog = dialog;
        this.container = dialog.composer;

        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.MetadataBundle", Locale.getDefault());
        categories = new Object[] {Microworld.getCategoryName(Microworld.GEOGRAPHY),
                                   Microworld.getCategoryName(Microworld.GEOMETRY),
                                   Microworld.getCategoryName(Microworld.HISTORY),
                                   Microworld.getCategoryName(Microworld.MATHEMATICS),
                                   Microworld.getCategoryName(Microworld.PHYSICS),
                                   Microworld.getCategoryName(Microworld.CROSS_SUBJECT)};


        titleLabel = new JLabel(bundle.getString("Title"));
        subjectLabel = new JLabel(bundle.getString("Subject"));
        companyLabel = new JLabel(bundle.getString("Company"));
        categoryLabel = new JLabel(bundle.getString("Category"));
        keywordLabel = new JLabel(bundle.getString("Keyword"));
        commentLabel = new JLabel(bundle.getString("Comment"));
        authorLabel = new JLabel(bundle.getString("Author"));
        lockLabel = new JLabel(bundle.getString("LockLabel"));

        // EXPLICIT START
        ExplicitLayout el = new ExplicitLayout();
        setLayout(el);

        Component[] labelGroup = {titleLabel, subjectLabel, companyLabel, categoryLabel,
                                  keywordLabel, commentLabel, authorLabel, lockLabel};

        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
//        Expression maxLabelWidthExp = MathEF.max(GroupEF.createExpressions(ComponentEF.preferredWidth(null), labelGroup));
        Expression maxLabelWidthExp = GroupEF.preferredWidthMax(labelGroup);
        ExplicitConstraints ec1 = new ExplicitConstraints(titleLabel);
        ec1.setX(ContainerEF.left(titleLabel));
        ec1.setWidth(maxLabelWidthExp);
        ec1.setY(ContainerEF.top(titleLabel));
        ec1.setHeight(MathEF.constant(FIELD_HEIGHT));
        add(titleLabel, ec1);

        titleFld = new JTextField();
        ExplicitConstraints ec3 = new ExplicitConstraints(titleFld);
        ec3.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec3.setWidth(ContainerEF.width(titleFld).subtract(maxLabelWidthExp).subtract(H_GAP));
        ec3.setX(ComponentEF.right(titleLabel).add(H_GAP));
        ec3.setY(ComponentEF.top(titleLabel));
        add(titleFld, ec3);

        ExplicitConstraints ec2 = new ExplicitConstraints(subjectLabel);
        ec2.setX(ContainerEF.left(subjectLabel));
        ec2.setY(ComponentEF.bottom(titleLabel).add(V_GAP));
        ec2.setWidth(maxLabelWidthExp);
        ec2.setHeight(MathEF.constant(FIELD_HEIGHT));
        add(subjectLabel, ec2);

        subjectFld = new JTextField();
        ExplicitConstraints ec4 = new ExplicitConstraints(subjectFld);
        ec4.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec4.setWidth(ContainerEF.width(subjectFld).subtract(maxLabelWidthExp).subtract(H_GAP));
        ec4.setX(ComponentEF.right(subjectLabel).add(H_GAP));
        ec4.setY(ComponentEF.top(subjectLabel));
        add(subjectFld, ec4);

        ExplicitConstraints ec5 = new ExplicitConstraints(authorLabel);
        ec5.setX(ContainerEF.left(authorLabel));
        ec5.setY(ComponentEF.bottom(subjectLabel).add(V_GAP));
        ec5.setWidth(maxLabelWidthExp);
        ec5.setHeight(MathEF.constant(80));
        add(authorLabel, ec5);

        authorsList = new JList() {
            public String getToolTipText(MouseEvent event) {
                int index = locationToIndex(event.getPoint());
                if (index == -1)
                    return bundle.getString("AuthorTip");
                else
                    return (String) getModel().getElementAt(index);
            }
        };
        authorsList.setModel(authorListModel);
        authorsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        authorScrollPane = new JScrollPane(authorsList);
        addAuthorButton = new JButton(bundle.getString("AddAuthor"));
        remAuthorButton = new JButton(bundle.getString("RemoveAuthor"));
        Insets buttonInsets = remAuthorButton.getMargin();
        buttonInsets.left = 1;
        buttonInsets.right = 1;
        addAuthorButton.setMargin(buttonInsets);
        remAuthorButton.setMargin(buttonInsets);
        addAuthorButton.setForeground(ESlateContainerUtils.color128);
        remAuthorButton.setForeground(ESlateContainerUtils.color128);

        Expression addButtonPanelWidth = ComponentEF.width(addAuthorButton).add(H_GAP);
        Expression addButtonHeight = ComponentEF.height(addAuthorButton);

        ExplicitConstraints ec6 = new ExplicitConstraints(authorScrollPane);
        ec6.setX(ComponentEF.right(authorLabel).add(H_GAP));
        ec6.setY(ComponentEF.bottom(subjectFld).add(V_GAP));
        ec6.setWidth(ContainerEF.width(authorScrollPane).subtract(maxLabelWidthExp).subtract(addButtonPanelWidth).subtract(H_GAP));
        ec6.setHeight(MathEF.constant(80));
        add(authorScrollPane, ec6);

        ExplicitConstraints ec7 = new ExplicitConstraints(addAuthorButton);
        ec7.setX(ComponentEF.right(authorScrollPane).add(H_GAP));
        ec7.setY(ComponentEF.top(authorScrollPane).add(MathEF.constant(80).subtract(addButtonHeight).subtract(addButtonHeight).subtract(2).divide(2)));
        add(addAuthorButton, ec7);

        ExplicitConstraints ec8 = new ExplicitConstraints(remAuthorButton);
        ec8.setX(ComponentEF.right(authorScrollPane).add(H_GAP));
        ec8.setY(ComponentEF.bottom(addAuthorButton).add(4));
        ec8.setWidth(ComponentEF.width(addAuthorButton));
        ec8.setHeight(ComponentEF.height(addAuthorButton));
        add(remAuthorButton, ec8);

        ExplicitConstraints ec9 = new ExplicitConstraints(companyLabel);
        ec9.setX(ContainerEF.left(companyLabel));
        ec9.setY(ComponentEF.bottom(authorLabel).add(V_GAP));
        ec9.setWidth(maxLabelWidthExp);
        ec9.setHeight(MathEF.constant(FIELD_HEIGHT));
        add(companyLabel, ec9);

        companyFld = new JTextField();
        ExplicitConstraints ec10 = new ExplicitConstraints(companyFld);
        ec10.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec10.setWidth(ContainerEF.width(companyFld).subtract(maxLabelWidthExp).subtract(H_GAP));
        ec10.setX(ComponentEF.right(companyLabel).add(H_GAP));
        ec10.setY(ComponentEF.top(companyLabel));
        add(companyFld, ec10);

        ExplicitConstraints ec11 = new ExplicitConstraints(categoryLabel);
        ec11.setX(ContainerEF.left(categoryLabel));
        Expression bigGapExp = MathEF.multiply(MathEF.constant(V_GAP), 4);
        ec11.setY(ComponentEF.bottom(companyLabel).add(bigGapExp));
        ec11.setWidth(maxLabelWidthExp);
        ec11.setHeight(MathEF.constant(FIELD_HEIGHT));
        add(categoryLabel, ec11);

        categoryBox = new JComboBox(categories);
        categoryBox.setEditable(true);
        ExplicitConstraints ec12 = new ExplicitConstraints(categoryBox);
        ec12.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec12.setWidth(ContainerEF.width(categoryBox).subtract(maxLabelWidthExp).subtract(H_GAP));
        ec12.setX(ComponentEF.right(categoryLabel).add(H_GAP));
        ec12.setY(ComponentEF.top(categoryLabel));
        add(categoryBox, ec12);

        ExplicitConstraints ec13 = new ExplicitConstraints(keywordLabel);
        ec13.setX(ContainerEF.left(keywordLabel));
        ec13.setY(ComponentEF.bottom(categoryLabel).add(V_GAP));
        ec13.setWidth(maxLabelWidthExp);
        ec13.setHeight(MathEF.constant(FIELD_HEIGHT));
        add(keywordLabel, ec13);

        keywordFld = new JTextField();
        ExplicitConstraints ec14 = new ExplicitConstraints(keywordFld);
        ec14.setHeight(MathEF.constant(FIELD_HEIGHT));
        ec14.setWidth(ContainerEF.width(keywordFld).subtract(maxLabelWidthExp).subtract(H_GAP));
        ec14.setX(ComponentEF.right(keywordLabel).add(H_GAP));
        ec14.setY(ComponentEF.top(keywordLabel));
        add(keywordFld, ec14);

        lockButton = new JButton(bundle.getString("Lock"));
        lockButton.setForeground(ESlateContainerUtils.color128);
        lockButton.setAlignmentX(CENTER_ALIGNMENT);
        ExplicitConstraints ec17 = new ExplicitConstraints(lockButton);
        ec17.setOriginX(ExplicitConstraints.CENTER);
        Expression e1 = ContainerEF.width(this).subtract(maxLabelWidthExp);
        e1 = MathEF.divide(e1, 2);
        e1 = MathEF.add(e1, maxLabelWidthExp).add(H_GAP);
        ec17.setX(e1);
        Expression lockButtonGap = MathEF.multiply(MathEF.constant(V_GAP), 2);
        ec17.setY(ContainerEF.bottom(this).subtract(lockButtonGap).subtract(ComponentEF.height(lockButton)));
        add(lockButton, ec17);

        ExplicitConstraints ec171 = new ExplicitConstraints(lockLabel);
        ec171.setX(ContainerEF.left(this));
        ec171.setOriginY(ExplicitConstraints.CENTER);
        Expression exp = ComponentEF.top(lockButton).add(ComponentEF.height(lockButton).divide(2));
        ec171.setY(exp);
        ec171.setWidth(maxLabelWidthExp);
        ec171.setHeight(MathEF.constant(FIELD_HEIGHT));
        add(lockLabel, ec171);

        ExplicitConstraints ec15 = new ExplicitConstraints(commentLabel);
        ec15.setX(ContainerEF.left(this));
        ec15.setY(ComponentEF.bottom(keywordLabel).add(V_GAP));
        ec15.setWidth(maxLabelWidthExp);
        ec15.setHeight(ComponentEF.top(lockButton).subtract(ComponentEF.top(commentLabel)).subtract(lockButtonGap));
//        ec15.setHeight(MathEF.constant(200));
        add(commentLabel, ec15);

        commentsArea = new JTextArea();
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        commentsScrollPane = new JScrollPane(commentsArea);
        ExplicitConstraints ec16 = new ExplicitConstraints(commentsScrollPane);
//        ec16.setHeight(MathEF.constant(200));
        ec16.setY(ComponentEF.top(commentLabel));
        ec16.setHeight(ComponentEF.top(lockButton).subtract(ComponentEF.top(commentsScrollPane)).subtract(lockButtonGap));
        ec16.setWidth(ContainerEF.width(commentsScrollPane).subtract(maxLabelWidthExp).subtract(H_GAP));
        ec16.setX(ComponentEF.right(keywordLabel).add(H_GAP));
        add(commentsScrollPane, ec16);

        Expression newPanelWidth = maxLabelWidthExp.add(H_GAP).add(MathEF.constant(250).add(ComponentEF.width(addAuthorButton)));
        el.setPreferredLayoutSize(newPanelWidth, ContainerEF.height(this));
        // EXPLICIT END

        titleLabel.setToolTipText(bundle.getString("TitleTip"));
        titleFld.setToolTipText(bundle.getString("TitleTip"));
        subjectLabel.setToolTipText(bundle.getString("SubjectTip"));
        subjectFld.setToolTipText(bundle.getString("SubjectTip"));
        companyLabel.setToolTipText(bundle.getString("CompanyTip"));
        companyFld.setToolTipText(bundle.getString("CompanyTip"));
        categoryLabel.setToolTipText(bundle.getString("CategoryTip"));
        categoryBox.setToolTipText(bundle.getString("CategoryTip"));
        keywordLabel.setToolTipText(bundle.getString("KeywordTip"));
        keywordFld.setToolTipText(bundle.getString("KeywordTip"));
        commentLabel.setToolTipText(bundle.getString("CommentTip"));
        commentsArea.setToolTipText(bundle.getString("CommentTip"));
        authorLabel.setToolTipText(bundle.getString("AuthorTip"));
        authorsList.setToolTipText(bundle.getString("AuthorTip"));
        lockLabel.setToolTipText(bundle.getString("LockTip"));
        lockButton.setToolTipText(bundle.getString("LockTip"));
        addAuthorButton.setToolTipText(bundle.getString("AddAuthorTip"));
        remAuthorButton.setToolTipText(bundle.getString("RemoveAuthorTip"));


/*        Dimension labelDim = new Dimension(maxLabelWidth+5, FIELD_HEIGHT);
        titleLabel.setMaximumSize(labelDim); titleLabel.setPreferredSize(labelDim); titleLabel.setMinimumSize(labelDim);
        subjectLabel.setMaximumSize(labelDim); subjectLabel.setPreferredSize(labelDim); subjectLabel.setMinimumSize(labelDim);
        companyLabel.setMaximumSize(labelDim); companyLabel.setPreferredSize(labelDim); companyLabel.setMinimumSize(labelDim);
        categoryLabel.setMaximumSize(labelDim); categoryLabel.setPreferredSize(labelDim); categoryLabel.setMinimumSize(labelDim);
        keywordLabel.setMaximumSize(labelDim); keywordLabel.setPreferredSize(labelDim); keywordLabel.setMinimumSize(labelDim);
        commentLabel.setMaximumSize(labelDim); commentLabel.setPreferredSize(labelDim); commentLabel.setMinimumSize(labelDim);
        authorLabel.setMaximumSize(labelDim); authorLabel.setPreferredSize(labelDim); authorLabel.setMinimumSize(labelDim);

        titleFld = new StandardHeightTextField(FIELD_HEIGHT);
        titleFld.setAlignmentY(CENTER_ALIGNMENT);
        titleLabel.setAlignmentY(CENTER_ALIGNMENT);
        FixedHeightPanel titlePanel = new FixedHeightPanel(20);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(titleLabel);
        titlePanel.add(titleFld);

        subjectFld = new StandardHeightTextField(FIELD_HEIGHT);
        subjectFld.setAlignmentY(CENTER_ALIGNMENT);
        subjectLabel.setAlignmentY(CENTER_ALIGNMENT);
        FixedHeightPanel subjectPanel = new FixedHeightPanel(20);
        subjectPanel.setLayout(new BoxLayout(subjectPanel, BoxLayout.X_AXIS));
        subjectPanel.add(subjectLabel);
        subjectPanel.add(subjectFld);

        authorsList = new JList();
        authorScrollPane = new JScrollPane(authorsList) {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = 10;
                return dim;
            }
        };

        JButton addAuthorButton = new JButton(bundle.getString("AddAuthor"));
        JButton remAuthorButton = new JButton(bundle.getString("RemoveAuthor"));
        Insets buttonInsets = remAuthorButton.getMargin();
        buttonInsets.left = 1;
        buttonInsets.right = 1;
        addAuthorButton.setMargin(buttonInsets);
        remAuthorButton.setMargin(buttonInsets);
        addAuthorButton.setForeground(ESlateContainerUtils.color128);
        remAuthorButton.setForeground(ESlateContainerUtils.color128);

        JPanel authorButtonPanel = new JPanel();
        authorButtonPanel.setLayout(new BorderLayout(0, 0)); //BoxLayout(authorButtonPanel, BoxLayout.Y_AXIS));
        authorButtonPanel.add(addAuthorButton, BorderLayout.NORTH);
        authorButtonPanel.add(remAuthorButton, BorderLayout.SOUTH);

        int authorButtonPanelHeight = 2 * addAuthorButton.getPreferredSize().height + 4; //4 is the gap between the buttons
        JPanel abp2 = new JPanel();
        abp2.setLayout(new BoxLayout(abp2, BoxLayout.Y_AXIS));
        abp2.add(Box.createVerticalStrut((60-authorButtonPanelHeight)/2));
        abp2.add(authorButtonPanel);
        abp2.add(Box.createVerticalStrut((60-authorButtonPanelHeight)/2));

        JPanel abp3 = new JPanel();
        abp3.setLayout(new BorderLayout(5, 0));
        abp3.add(authorScrollPane, BorderLayout.CENTER);
        abp3.add(abp2, BorderLayout.EAST);

        FixedHeightPanel authorPanel = new FixedHeightPanel(60);
        authorPanel.setLayout(new BoxLayout(authorPanel, BoxLayout.X_AXIS));
        authorPanel.add(authorLabel);
        authorPanel.add(abp3);

        companyFld = new StandardHeightTextField(FIELD_HEIGHT);
        FixedHeightPanel companyPanel = new FixedHeightPanel(20);
        companyPanel.setLayout(new BoxLayout(companyPanel, BoxLayout.X_AXIS));
        companyLabel.setAlignmentY(CENTER_ALIGNMENT);
        companyFld.setAlignmentY(CENTER_ALIGNMENT);
        companyPanel.add(companyLabel);
        companyPanel.add(companyFld);

        categoryBox = new JComboBox(categories);
        categoryBox.setEditable(true);
        FixedHeightPanel categoryPanel = new FixedHeightPanel(20);
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.X_AXIS));
        categoryBox.setAlignmentY(CENTER_ALIGNMENT);
        categoryLabel.setAlignmentY(CENTER_ALIGNMENT);
        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryBox);

        keywordFld = new StandardHeightTextField(FIELD_HEIGHT);
        FixedHeightPanel keywordPanel = new FixedHeightPanel(20);
        keywordPanel.setLayout(new BoxLayout(keywordPanel, BoxLayout.X_AXIS));
        keywordLabel.setAlignmentY(CENTER_ALIGNMENT);
        keywordFld.setAlignmentY(CENTER_ALIGNMENT);
        keywordPanel.add(keywordLabel);
        keywordPanel.add(keywordFld);

        commentsArea = new JTextArea();
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        commentsScrollPane = new JScrollPane(commentsArea) {
            public Dimension getMaximumSize() {
                Dimension dim = super.getMaximumSize();
                dim.height = 180;
                return dim;
            }
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = 180;
                return dim;
            }
        };
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.X_AXIS));
        commentsPanel.add(commentLabel);
        commentsPanel.add(commentsScrollPane);

        lockButton = new JButton(bundle.getString("Lock")) {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = 25;
                return dim;
            }
        };
        lockButton.setForeground(ESlateContainerUtils.color128);
        lockButton.setAlignmentX(CENTER_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(titlePanel);
        add(Box.createVerticalStrut(5));
        add(subjectPanel);
        add(Box.createVerticalStrut(5));
        add(authorPanel);
        add(Box.createVerticalStrut(5));
        add(companyPanel);
        add(Box.createVerticalStrut(5));
        add(categoryPanel);
        add(Box.createVerticalStrut(5));
        add(keywordPanel);
        add(Box.createVerticalStrut(5));
        add(commentsPanel);
//        add(Box.createVerticalStrut(10));
//        add(Box.createGlue());
        add(lockButton);
        add(Box.createVerticalStrut(10));
*/
        TitledBorder tb = new TitledBorder(bundle.getString("MicoworldInfo"));
        tb.setTitleColor(ESlateContainerUtils.titleBorderColor);
        setBorder(new CompoundBorder(tb, new EmptyBorder(0,5,5,5)));
//        setBorder(new CompoundBorder(new EmptyBorder(5, 0, 0, 0), new CompoundBorder(tb, new EmptyBorder(0,5,5,5))));

        addAuthorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AuthorDialog dialog = new AuthorDialog(container,
                                                       propertiesDialog,
                                                       null, null, null, true);
                if (dialog.getReturnCode() == AuthorDialog.DIALOG_OK) {
                    int index = authorListModel.add(dialog.getName(), dialog.getSurname(), dialog.getEmail());
                    authorsList.setSelectedIndex(index);
                    authorsList.scrollRectToVisible(authorsList.getCellBounds(index, index));
                }
            }
        });
        remAuthorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = authorsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    container.containerUtils.selectNewBeforeSelectedEntryIsRemoved(authorsList);
                    authorListModel.remove(selectedIndex);
                    if (authorsList.getSelectedIndex() == -1)
                        remAuthorButton.setEnabled(false);
                }
            }
        });


        authorsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2)
                    return;
//                    int index = list.locationToIndex(e.getPoint());
                int index = authorsList.getSelectedIndex();
                if (index == -1) return;
                String author = (String) authorListModel.authors.get(index); // (String) authorsList.getSelectedValue();
                if (author == null || author.trim().length() == 0)
                    return;
                String name = (String) authorListModel.names.get(index);
                String surname = (String) authorListModel.surnames.get(index);
                String email = (String) authorListModel.emails.get(index);
                AuthorDialog dialog = new AuthorDialog(container,
                                                       propertiesDialog,
                                                       name, surname, email, true);
                if (dialog.getReturnCode() == AuthorDialog.DIALOG_OK) {
                    authorListModel.put(dialog.getName(), dialog.getSurname(), dialog.getEmail(), index);
                }
            }
        });

        authorsList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                if (authorsList.getSelectedIndex() == -1)
                    remAuthorButton.setEnabled(false);
                else
                    remAuthorButton.setEnabled(true);
            }
        });

        lockButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (password == null || password.trim().length() == 0) {
                    lockMicroworld();
                }else{
                    if (container.microworld.isLocked()) {
                        unlockMicroworld();
                    }else{
                        lockMicroworld();
                    }
                }

            }
        });

        // Initialization
        remAuthorButton.setEnabled(false);
        if (!container.microworld.microworldNameUserDefined) {
            titleFld.setEnabled(false);
            titleLabel.setEnabled(false);
        }
        setMicroworldTitle(container.microworld.getTitle());
        setMicroworldSubject(container.microworld.getSubject());
        setMicroworldCompany(container.microworld.getCompany());
        setMicroworldAuthors(container.microworld.getAuthors());
        setMicroworldkeywords(container.microworld.getKeywords());
        setMicroworldComments(container.microworld.getComments());
        setMicroworldCategory(container.microworld.getCategoryName());
        setPassword(container.microworld.getSubTitle());
//        noMwdPassword = (container.microworld.getSubTitle() == null || container.microworld.getSubTitle().trim().length() == 0);

    }

    void lockMicroworld() {
        PasswordDialog dialog = new PasswordDialog(container,
                                                   propertiesDialog,
                                                   password,
                                                   password,
                                                   true,
                                                   PasswordDialog.LOCK_MODE);
        if (dialog.getReturnCode() != PasswordDialog.DIALOG_OK)
            return;
        String p1 = dialog.getPassword();
        String p2 = dialog.getPassword2();
        if (p1 == null && p2 == null) {
            password = null;
            container.microworld.setLocked(false);
            propertiesDialog.setLocked(false);
        }else if (p1 != null && p2 != null && p1.equals(p2)) {
            password = p1;
            container.microworld.setLocked(true);
            propertiesDialog.setLocked(true);
//            setLocked(true);
        }else
            ESlateOptionPane.showMessageDialog(propertiesDialog,
                                               bundle.getString("InvalidConfirmation"),
                                               container.containerBundle.getString("Error"),
                                               JOptionPane.ERROR_MESSAGE);
    }

    /** Unlocks the microworld, if it is protected by a password.
     */
    boolean unlockMicroworld() {
        PasswordDialog dialog = new PasswordDialog(container,
                                                   propertiesDialog,
                                                   null,
                                                   null,
                                                   true,
                                                   PasswordDialog.UNLOCK_MODE);
        if (dialog.getReturnCode() != PasswordDialog.DIALOG_OK)
            return false;
        String p1 = dialog.getPassword();
        if (p1 != null && p1.equals(password)) {
            container.microworld.setLocked(false);
            propertiesDialog.setLocked(false);
            return true;
        }else{
            ESlateOptionPane.showMessageDialog(propertiesDialog,
                                               bundle.getString("InvalidPassword"),
                                               container.containerBundle.getString("Error"),
                                               JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    void setLocked(boolean locked) {
        titleFld.setEnabled(!locked);
        subjectFld.setEnabled(!locked);
        companyFld.setEnabled(!locked);
        keywordFld.setEnabled(!locked);
        authorsList.setEnabled(!locked);
        categoryBox.setEnabled(!locked);
        commentsArea.setEnabled(!locked);
        titleLabel.setEnabled(!locked);
        subjectLabel.setEnabled(!locked);
        companyLabel.setEnabled(!locked);
        categoryLabel.setEnabled(!locked);
        keywordLabel.setEnabled(!locked);
        commentLabel.setEnabled(!locked);
        authorLabel.setEnabled(!locked);
        remAuthorButton.setEnabled(!locked);
        addAuthorButton.setEnabled(!locked);
        if (locked)
            lockButton.setText(bundle.getString("Unlock"));
        else
            lockButton.setText(bundle.getString("Lock"));
    }

    void setPassword(String password) {
        this.password = password;
    }

    String getPassword() {
        return password;
    }

    public void setMicroworldTitle(String title) {
        titleFld.setText(title);
    }
    public String getMicroworldTitle() {
        return titleFld.getText();
    }

    public void setMicroworldSubject(String subject) {
        subjectFld.setText(subject);
    }
    public String getMicroworldSubject() {
        return subjectFld.getText();
    }

    public void setMicroworldCompany(String company) {
        companyFld.setText(company);
    }
    public String getMicroworldCompany() {
        return companyFld.getText();
    }

    public void setMicroworldkeywords(String keywords) {
        keywordFld.setText(keywords);
    }
    public String getMicroworldkeywords() {
        return keywordFld.getText();
    }

    public void setMicroworldAuthors(MicroworldAuthor[] authors) {
        ((AuthorListModel) authorsList.getModel()).setAuthors(authors);
    }
    public MicroworldAuthor[] getMicroworldAuthors() {
        return ((AuthorListModel) authorsList.getModel()).getAuthors();
    }

    public void setMicroworldCategory(String category) {
        categoryBox.setSelectedItem(category);
    }
    public String getMicroworldCategory() {
        return (String) categoryBox.getSelectedItem();
    }

    public void setMicroworldComments(String comments) {
        commentsArea.setText(comments);
    }
    public String getMicroworldComments() {
        return commentsArea.getText();
    }

    void applyMicroworldMetadata() {
        if (!container.microworld.setTitle(getMicroworldTitle()))
            setMicroworldTitle(container.microworld.getTitle());
        container.microworld.setSubject(getMicroworldSubject());
        container.microworld.setCompany(getMicroworldCompany());
        container.microworld.setAuthors(getMicroworldAuthors());
        container.microworld.setKeywords(getMicroworldkeywords());
        container.microworld.setComments(getMicroworldComments());
        container.microworld.setCategoryName(getMicroworldCategory());
        container.microworld.setSubTitle(getPassword());
    }
}

// AuthorDialog
class AuthorDialog extends JDialog {
    public static final int DIALOG_CANCELLED = 1;
    public static final int DIALOG_OK = 0;
    public static final int H_GAP = 5;
    public static final int V_GAP = 5;
    public static final int TEXT_FIELD_WIDTH = 180;
    String name, surName, email;
    JLabel nameLb, surnameLb, emailLb;
    JTextField nameFld, surnameFld, emailFld;
    JButton okButton, cancelButton;
    ResourceBundle bundle;
    int returnCode = DIALOG_CANCELLED;

    public AuthorDialog(final ESlateContainer container, JDialog parentDialog, String name, String surname, String email, boolean editEnabled) {
        super(parentDialog, true);
        bundle = ResourceBundle.getBundle("gr.cti.eslate.base.container.AuthorDialogBundle", Locale.getDefault());
        setTitle(bundle.getString("Title"));

        JPanel mainPanel = new JPanel();
        ExplicitLayout el = new ExplicitLayout();
        mainPanel.setLayout(el);

        nameLb = new JLabel(bundle.getString("Name"));
        surnameLb = new JLabel(bundle.getString("Surname"));
        emailLb = new JLabel(bundle.getString("E-Mail"));

        nameFld = new JTextField();
        surnameFld = new JTextField();
        emailFld = new JTextField();

        okButton = new JButton(bundle.getString("OK"));
        cancelButton = new JButton(bundle.getString("Cancel"));
        okButton.setForeground(ESlateContainerUtils.color128);
        cancelButton.setForeground(ESlateContainerUtils.color128);
        Insets zeroInsets = new Insets(0, 0, 0, 0);
        okButton.setMargin(zeroInsets);
        cancelButton.setMargin(zeroInsets);

        nameLb.setToolTipText(bundle.getString("NameTip"));
        surnameLb.setToolTipText(bundle.getString("SurnameTip"));
        emailLb.setToolTipText(bundle.getString("E-MailTip"));
        nameFld.setToolTipText(bundle.getString("NameTip"));
        surnameFld.setToolTipText(bundle.getString("SurnameTip"));
        emailFld.setToolTipText(bundle.getString("E-MailTip"));

        int fieldHeight = nameLb.getPreferredSize().height;
        if (nameFld.getPreferredSize().height > fieldHeight)
            fieldHeight = nameFld.getPreferredSize().height;

        Expression fieldHeightExp = MathEF.constant(fieldHeight);
        Expression vgapExp = MathEF.constant(V_GAP);
        Expression hgapExp = MathEF.constant(H_GAP);

        ExplicitConstraints ec1 = new ExplicitConstraints(nameLb);
        ec1.setX(ContainerEF.left(mainPanel));
        ec1.setY(ContainerEF.top(mainPanel));
        ec1.setHeight(fieldHeightExp);

        ExplicitConstraints ec2 = new ExplicitConstraints(surnameLb);
        ec2.setX(ContainerEF.left(mainPanel));
        ec2.setY(ComponentEF.bottom(nameLb).add(V_GAP));
        ec2.setHeight(fieldHeightExp);

        ExplicitConstraints ec3 = new ExplicitConstraints(emailLb);
        ec3.setX(ContainerEF.left(mainPanel));
        ec3.setY(ComponentEF.bottom(surnameLb).add(V_GAP));
        ec3.setHeight(fieldHeightExp);

        Component[] labels = new Component[] {nameLb, surnameLb, emailLb};
        Expression maxLabelWidth  =  GroupEF.preferredWidthMax(labels);
        Expression secColumnXPos = ContainerEF.left(mainPanel).add(maxLabelWidth).add(H_GAP);
        ExplicitConstraints ec4 = new ExplicitConstraints(nameFld);
        Expression fieldWidthExp = MathEF.constant(TEXT_FIELD_WIDTH);
        ec4.setX(secColumnXPos);
        ec4.setY(ComponentEF.top(nameLb));
        ec4.setWidth(ContainerEF.width(mainPanel).subtract(secColumnXPos).add(ContainerEF.left(mainPanel)));
        ec4.setHeight(fieldHeightExp);

        ExplicitConstraints ec5 = new ExplicitConstraints(surnameFld);
        ec5.setX(secColumnXPos);
        ec5.setY(ComponentEF.top(surnameLb));
        ec5.setWidth(ContainerEF.width(mainPanel).subtract(secColumnXPos).add(ContainerEF.left(mainPanel)));
        ec5.setHeight(fieldHeightExp);

        ExplicitConstraints ec6 = new ExplicitConstraints(emailFld);
        ec6.setX(secColumnXPos);
        ec6.setY(ComponentEF.top(emailLb));
        ec6.setWidth(ContainerEF.width(mainPanel).subtract(secColumnXPos).add(ContainerEF.left(mainPanel)));
        ec6.setHeight(fieldHeightExp);

        Expression panelHeightExp = fieldHeightExp.multiply(3);
        panelHeightExp = panelHeightExp.add(V_GAP*3);
        Expression panelWidthExp = maxLabelWidth.add(hgapExp).add(fieldWidthExp);
        el.setPreferredLayoutSize(panelWidthExp, panelHeightExp);

        mainPanel.add(nameLb, ec1);
        mainPanel.add(surnameLb, ec2);
        mainPanel.add(emailLb, ec3);
        mainPanel.add(nameFld, ec4);
        mainPanel.add(surnameFld, ec5);
        mainPanel.add(emailFld, ec6);

        TitledBorder tb1 = new TitledBorder(bundle.getString("AuthorInfo"));
        tb1.setTitleColor(ESlateContainerUtils.titleBorderColor);
        mainPanel.setBorder(new CompoundBorder(tb1, new EmptyBorder(0,5,0,5)));

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

        el1.setPreferredLayoutSize(ContainerEF.width(getContentPane()), buttonHeightExp);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(mainPanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(5));

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                container.containerUtils.discardDialogButtonListeners(AuthorDialog.this);
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnCode = DIALOG_OK;
                dispose();
                container.containerUtils.discardDialogButtonListeners(AuthorDialog.this);
            }
        });

        surnameFld.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                String sn = getSurname();
                if (sn == null || sn.trim().length() == 0)
                    okButton.setEnabled(false);
                else
                    okButton.setEnabled(true);
            }
            public void removeUpdate(DocumentEvent e) {
                String sn = getSurname();
                if (sn == null || sn.trim().length() == 0)
                    okButton.setEnabled(false);
                else
                    okButton.setEnabled(true);
            }
            public void changedUpdate(DocumentEvent e) {
                String sn = getSurname();
                if (sn == null || sn.trim().length() == 0)
                    okButton.setEnabled(false);
                else
                    okButton.setEnabled(true);
            }
        });

        // ESCAPE HANDLER
        container.containerUtils.attachDialogButtonListener(this, cancelButton, java.awt.event.KeyEvent.VK_ESCAPE);
        container.containerUtils.attachDialogButtonListener(this, okButton, java.awt.event.KeyEvent.VK_ENTER);

        // Initialization
        setName(name);
        setSurname(surname);
        setEmail(email);
        if (!editEnabled) {
            nameFld.setEnabled(false);
            surnameFld.setEnabled(false);
            emailFld.setEnabled(false);
        }

        ESlateContainerUtils.showDialog(this, parentDialog, true);
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getName() {
        return nameFld.getText();
    }

    public void setName(String name) {
        nameFld.setText(name);
    }

    public String getSurname() {
        return surnameFld.getText();
    }

    public void setSurname(String surname) {
        surnameFld.setText(surname);
        if (surname == null || surname.trim().length() == 0)
            okButton.setEnabled(false);
    }

    public String getEmail() {
        return emailFld.getText();
    }

    public void setEmail(String email) {
        emailFld.setText(email);
    }

}


// AuthorListModel
class AuthorListModel extends AbstractListModel {
    ArrayList names = new ArrayList();
    ArrayList surnames = new ArrayList();
    ArrayList emails = new ArrayList();
    ArrayList authors = new ArrayList();

    public AuthorListModel() {
    }
//    public void addListDataListener(ListDataListener l) {}
//    public void removeListDataListener(ListDataListener l) {}
    public Object getElementAt(int index) {
        return authors.get(index);
    }
    public int getSize() {
        return authors.size();
    }

    public int add(String name, String surname, String email) {
        surname = surname.trim();
        StringBuffer newAuthor = new StringBuffer(surname);
        name = name.trim();
        if (name != null && name.trim().length() != 0) {
            newAuthor.append(' ');
            newAuthor.append(name);
        }
        email = email.trim();
        if (email != null && email.trim().length() != 0) {
            newAuthor.append(" (");
            newAuthor.append(email);
            newAuthor.append(')');
        }
        authors.add(newAuthor.toString());
        names.add(name);
        surnames.add(surname);
        emails.add(email);
        int index = authors.size()-1;
        fireIntervalAdded(this, index, index);
        return index;
    }

    public void put(String name, String surname, String email, int index) {
        if (index < 0 || index >= authors.size())
            throw new ArrayIndexOutOfBoundsException("Invalid authr index: " + index);

        surname = surname.trim();
        StringBuffer newAuthor = new StringBuffer(surname);
        name = name.trim();
        if (name != null && name.trim().length() != 0) {
            newAuthor.append(' ');
            newAuthor.append(name);
        }
        email = email.trim();
        if (email != null && email.trim().length() != 0) {
            newAuthor.append(" (");
            newAuthor.append(email);
            newAuthor.append(')');
        }
        authors.set(index, newAuthor.toString());
        names.set(index, name);
        surnames.set(index, surname);
        emails.set(index, email);
        fireContentsChanged(this, index, index);
    }

    public void remove(int index) {
        authors.remove(index);
        names.remove(index);
        surnames.remove(index);
        emails.remove(index);
        fireIntervalRemoved(this, index, index);
    }

    public MicroworldAuthor[] getAuthors() {
        MicroworldAuthor[] authors = new MicroworldAuthor[surnames.size()];
        for (int i=0; i<surnames.size(); i++) {
            MicroworldAuthor author = new MicroworldAuthor(names.get(i).toString(), surnames.get(i).toString());
            author.setEmail(emails.get(i).toString());
            authors[i] = author;
        }
        return authors;
    }

    public void setAuthors(MicroworldAuthor[] authors) {
        if (authors == null) return;
        for (int i=0; i<authors.length; i++)
            add(authors[i].getName(), authors[i].getSurname(), authors[i].getEmail());
    }
}
