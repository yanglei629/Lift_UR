package com.yanglei.LIFT.impl.installation;

import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;
import com.yanglei.LIFT.impl.Style;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LiftInstallationNodeView implements SwingInstallationNodeView<LiftInstallationNodeContribution> {

    private Style style;
    private JTextField ipField;
    private static final String logoFilePath = "/logo/logo_backyard.jpg";

    private JLabel stateMessage;
    private JLabel ipLabel;
    private JButton connectBtn;
    private JButton bUp;
    private JButton bDown;
    private JButton zeroCalibBtn;
    private Box modeBox;
    private JLabel controlModel;
    private JButton disconnectBtn;
    private JButton stopBtn;
    private LiftInstallationNodeContribution contribution;
    private JComboBox modeComboBox;
    private JButton cancelStopBtn;

    /**
     * status ui component
     */
    private JLabel statusHeader;
    private JLabel labelCurrentPos;
    private JLabel labelMovingStatus;
    private Box controlBox;
    private JPanel root;
    private JTextField ip_field;
    private JTextField port_field;
    private JButton connect_btn;
    private JCheckBox low_virtual_limit_checkbox;
    private JTextField low_virtual_limit_field;
    private JTextField high_virtual_limit_field;
    private JButton reset_btn;
    private JLabel connect_status_label;
    private JCheckBox high_virtual_limit_checkbox;
    private JComboBox stroke_box;
    private JLabel height_feedback_field;
    private JLabel speed_feedback_field;
    private JLabel status_feedback_field;
    private JLabel current_feedback_field;
    private JLabel temperature_feedback_field;
    private JLabel errorcode_feedback_field;
    private JLabel ip_label;
    private JLabel port_label;
    private JLabel stroke_label;
    private JLabel height_feedback_label;
    private JLabel speed_feedback_label;
    private JLabel status_feedback_label;
    private JLabel current_feedback_label;
    private JLabel temperature_feedback_label;
    private JLabel errorcode_feedback_label;
    private JPanel connection_info_panel;
    private JPanel virutual_limit_info_panel;
    private JPanel lift_info_panel;
    private JPanel servo_info_panel;
    private JLabel logo;
    private JLabel low_virtual_limit_label;
    private JLabel high_virtual_limit_label;
    private JPanel stroke_panel;

    public LiftInstallationNodeView(Style style) {
        this.style = style;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LiftInstallationNodeView");
        frame.setContentPane(new LiftInstallationNodeView().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public LiftInstallationNodeView() {
    }

    @Override
    public void buildUI(JPanel panel, LiftInstallationNodeContribution contribution) {
        this.contribution = contribution;

        panel.setLayout(new BorderLayout());
        panel.add($$$getRootComponent$$$(), BorderLayout.CENTER);
        setUI();
    }

    private void setUI() {
        // i18n
        stroke_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), this.contribution.getTextResource().stroke(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        connection_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), this.contribution.getTextResource().connectionInfo(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        virutual_limit_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), this.contribution.getTextResource().virtualLimit(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        lift_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), this.contribution.getTextResource().liftInfo(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        servo_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), this.contribution.getTextResource().servoInfo(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));


        ip_label.setText(this.contribution.getTextResource().ip());
        port_label.setText(this.contribution.getTextResource().port());
        stroke_label.setText(this.contribution.getTextResource().stroke());
        connect_btn.setText(this.contribution.getTextResource().connect());
        //reset_btn.setText(this.contribution.getTextResource().reset());
        high_virtual_limit_checkbox.setText(this.contribution.getTextResource().highVirtualLimit());
        low_virtual_limit_checkbox.setText(this.contribution.getTextResource().lowVirtualLimit());
        high_virtual_limit_label.setText(this.contribution.getTextResource().highVirtualLimit());
        low_virtual_limit_label.setText(this.contribution.getTextResource().lowVirtualLimit());
        height_feedback_label.setText(this.contribution.getTextResource().currentPos());
        speed_feedback_label.setText(this.contribution.getTextResource().currentSpeed());
        status_feedback_label.setText(this.contribution.getTextResource().status());
        current_feedback_label.setText(this.contribution.getTextResource().currentCurrent());
        temperature_feedback_label.setText(this.contribution.getTextResource().currentTemperature());
        errorcode_feedback_label.setText(this.contribution.getTextResource().errorCode());

        // set listener
        ip_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (contribution.isConnect()) {
                    return;
                }
                KeyboardTextInput keyboardInput = contribution.getKeyboardFactory().createIPAddressKeyboardInput();
                keyboardInput.setInitialValue(contribution.getIP());
                keyboardInput.show(ip_field, new KeyboardInputCallback<String>() {
                    @Override
                    public void onOk(String value) {
                        ip_field.setText(value);
                        contribution.setIP(value);
                    }
                });
            }
        });

        port_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (contribution.isConnect()) {
                    return;
                }
                Integer min = 0;
                Integer max = 99999;
                KeyboardNumberInput<Integer> keyboardInput = contribution.getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(contribution.getPort());
                keyboardInput.setErrorValidator(new InputValidator<Integer>() {
                    @Override
                    public boolean isValid(Integer value) {
                        if (value < min) {
                            return false;
                        }
                        if (value > max) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public String getMessage(Integer value) {
                        if (value < min) {
                            return "Illegal input";
                        }
                        if (value > max) {
                            return "Illegal input";
                        }
                        return "ok";
                    }
                });
                keyboardInput.show(port_field, new KeyboardInputCallback<Integer>() {
                    @Override
                    public void onOk(Integer value) {
                        port_field.setText(String.valueOf(value));
                        contribution.setPort(value);
                    }
                });
            }
        });

        stroke_box.removeAllItems();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("500");
        model.addElement("600");
        stroke_box.setModel(model);
        stroke_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object selectedItem = stroke_box.getSelectedItem();
                String select = (String) selectedItem;
                System.out.println(select);
                contribution.setStroke(Integer.valueOf(select));
            }
        });

        connect_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                if (contribution.getConnectionStatus()) {
                    contribution.disConnect();
                } else {
                    contribution.connect();
                }
            }
        });

        low_virtual_limit_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (contribution.isConnect()) {
                    return;
                }
                KeyboardNumberInput<Integer> keyboardInput = contribution.getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(contribution.getLowVirtualLimit());
                keyboardInput.show(low_virtual_limit_field, new KeyboardInputCallback<Integer>() {
                    @Override
                    public void onOk(Integer value) {
                        low_virtual_limit_field.setText(String.valueOf(value));
                        contribution.setLowVirtualLimit(value);
                    }
                });
            }
        });

        high_virtual_limit_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (contribution.isConnect()) {
                    return;
                }
                KeyboardNumberInput<Integer> keyboardInput = contribution.getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(contribution.getHighVirtualLimit());
                Integer min = contribution.getLowVirtualLimit();
                Integer max = contribution.getStroke();
                keyboardInput.setErrorValidator(new InputValidator<Integer>() {
                    @Override
                    public boolean isValid(Integer value) {
                        if (value < min) {
                            return false;
                        }
                        if (value > max) {
                            return false;
                        }
                        return true;
                    }

                    @Override
                    public String getMessage(Integer value) {
                        if (value < min) {
                            return "Illegal input";
                        }
                        if (value > max) {
                            return "Illegal input";
                        }
                        return "ok";
                    }
                });
                keyboardInput.show(high_virtual_limit_field, new KeyboardInputCallback<Integer>() {
                    @Override
                    public void onOk(Integer value) {
                        high_virtual_limit_field.setText(String.valueOf(value));
                        contribution.setHighVirtualLimit(value);
                    }
                });
            }
        });
    }

    public void showIP(String text) {
        //ipField.setText(text);
        this.ip_field.setText(text);
    }

    private Image getImage(String filaPath) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(filaPath));

            return image;
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception while loading icon.", e);
        }
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public void setConnectBtn(String connectString) {
        connectBtn.setText(connectString);
    }

    public void setDisconnectBtn(String disconnectString) {
        disconnectBtn.setText(disconnectString);
    }

    public void setUpBtn(String up) {
        bUp.setText(up);
    }

    public void setDownBtn(String down) {
        bDown.setText(down);
    }

    public void setZeroCalibBtn(String zeroCalibration) {
        zeroCalibBtn.setText(zeroCalibration);
    }

    public void setIpLabel(String ipString) {
        ipLabel.setText(ipString);
    }

    public void setConnectStatusLabel(String text) {
        stateMessage.setText(text);
    }

    public void setControlModeLabel(String s) {
        controlModel.setText(s);
    }

    public void setDisconnect(Integer result, LiftInstallationNodeContribution installationNode) {
        System.out.println("Lift Connection Miss");
        setConnectStatusLabel(this.contribution.getTextResource().No_Connection());
        modeBox.setVisible(false);
        controlBox.setVisible(false);
    }

    public void setConnected(boolean b) {
        ip_field.enableInputMethods(!b);
        ip_field.setFocusable(!b);
        ip_field.setEditable(!b);
        ip_field.setEnabled(!b);
        port_field.setEnabled(!b);
        if (b) {
            connect_btn.setText(this.contribution.getTextResource().disconnect());
        } else {
            connect_btn.setText(this.contribution.getTextResource().connect());
        }
        stroke_box.setEnabled(!b);
        high_virtual_limit_field.setEnabled(!b);
        low_virtual_limit_field.setEnabled(!b);
        high_virtual_limit_checkbox.setEnabled(!b);
        low_virtual_limit_checkbox.setEnabled(!b);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new GridBagLayout());
        root.setAlignmentX(1.0f);
        root.setAlignmentY(1.0f);
        root.setBackground(new Color(-1));
        connection_info_panel = new JPanel();
        connection_info_panel.setLayout(new GridBagLayout());
        connection_info_panel.setBackground(new Color(-1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(connection_info_panel, gbc);
        connection_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "连接信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        connection_info_panel.add(panel1, gbc);
        ip_label = new JLabel();
        ip_label.setMaximumSize(new Dimension(40, 30));
        ip_label.setMinimumSize(new Dimension(40, 30));
        ip_label.setPreferredSize(new Dimension(40, 30));
        ip_label.setText("IP:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(ip_label, gbc);
        ip_field = new JTextField();
        ip_field.setMaximumSize(new Dimension(150, 30));
        ip_field.setMinimumSize(new Dimension(150, 30));
        ip_field.setPreferredSize(new Dimension(150, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(ip_field, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel1.add(spacer1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        connection_info_panel.add(panel2, gbc);
        port_label = new JLabel();
        port_label.setMaximumSize(new Dimension(40, 30));
        port_label.setMinimumSize(new Dimension(40, 30));
        port_label.setPreferredSize(new Dimension(40, 30));
        port_label.setText("Port:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(port_label, gbc);
        port_field = new JTextField();
        port_field.setMaximumSize(new Dimension(100, 30));
        port_field.setMinimumSize(new Dimension(100, 30));
        port_field.setPreferredSize(new Dimension(100, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(port_field, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        connection_info_panel.add(panel3, gbc);
        connect_btn = new JButton();
        connect_btn.setMaximumSize(new Dimension(150, 30));
        connect_btn.setMinimumSize(new Dimension(150, 30));
        connect_btn.setPreferredSize(new Dimension(150, 30));
        connect_btn.setText("连接");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(connect_btn, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 3;
        panel3.add(spacer2, gbc);
        connect_status_label = new JLabel();
        connect_status_label.setMaximumSize(new Dimension(120, 30));
        connect_status_label.setMinimumSize(new Dimension(120, 30));
        connect_status_label.setPreferredSize(new Dimension(120, 30));
        connect_status_label.setText("未连接");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(connect_status_label, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel3.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        connection_info_panel.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        connection_info_panel.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        connection_info_panel.add(spacer6, gbc);
        virutual_limit_info_panel = new JPanel();
        virutual_limit_info_panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(virutual_limit_info_panel, gbc);
        virutual_limit_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "虚拟限位", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        virutual_limit_info_panel.add(panel4, gbc);
        low_virtual_limit_checkbox = new JCheckBox();
        low_virtual_limit_checkbox.setMaximumSize(new Dimension(150, 30));
        low_virtual_limit_checkbox.setMinimumSize(new Dimension(150, 30));
        low_virtual_limit_checkbox.setPreferredSize(new Dimension(150, 30));
        low_virtual_limit_checkbox.setText("虚拟最低限位");
        low_virtual_limit_checkbox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(low_virtual_limit_checkbox, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer7, gbc);
        low_virtual_limit_field = new JTextField();
        low_virtual_limit_field.setMaximumSize(new Dimension(80, 30));
        low_virtual_limit_field.setMinimumSize(new Dimension(80, 30));
        low_virtual_limit_field.setPreferredSize(new Dimension(80, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(low_virtual_limit_field, gbc);
        final JLabel label1 = new JLabel();
        label1.setMaximumSize(new Dimension(60, 30));
        label1.setMinimumSize(new Dimension(60, 30));
        label1.setPreferredSize(new Dimension(60, 30));
        label1.setText("mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label1, gbc);
        low_virtual_limit_label = new JLabel();
        low_virtual_limit_label.setMaximumSize(new Dimension(150, 30));
        low_virtual_limit_label.setMinimumSize(new Dimension(150, 30));
        low_virtual_limit_label.setPreferredSize(new Dimension(150, 30));
        low_virtual_limit_label.setText("虚拟最低限位");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(low_virtual_limit_label, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        virutual_limit_info_panel.add(spacer8, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        virutual_limit_info_panel.add(panel5, gbc);
        high_virtual_limit_checkbox = new JCheckBox();
        high_virtual_limit_checkbox.setMaximumSize(new Dimension(150, 30));
        high_virtual_limit_checkbox.setMinimumSize(new Dimension(150, 30));
        high_virtual_limit_checkbox.setPreferredSize(new Dimension(150, 30));
        high_virtual_limit_checkbox.setText("虚拟最高限位");
        high_virtual_limit_checkbox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(high_virtual_limit_checkbox, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer9, gbc);
        high_virtual_limit_field = new JTextField();
        high_virtual_limit_field.setMaximumSize(new Dimension(80, 30));
        high_virtual_limit_field.setMinimumSize(new Dimension(80, 30));
        high_virtual_limit_field.setPreferredSize(new Dimension(80, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(high_virtual_limit_field, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel5.add(spacer10, gbc);
        final JLabel label2 = new JLabel();
        label2.setMaximumSize(new Dimension(60, 30));
        label2.setMinimumSize(new Dimension(60, 30));
        label2.setPreferredSize(new Dimension(60, 30));
        label2.setText("mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(label2, gbc);
        high_virtual_limit_label = new JLabel();
        high_virtual_limit_label.setMaximumSize(new Dimension(150, 30));
        high_virtual_limit_label.setMinimumSize(new Dimension(150, 30));
        high_virtual_limit_label.setPreferredSize(new Dimension(150, 30));
        high_virtual_limit_label.setText("虚拟最高限位");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(high_virtual_limit_label, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        virutual_limit_info_panel.add(spacer11, gbc);
        lift_info_panel = new JPanel();
        lift_info_panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(lift_info_panel, gbc);
        lift_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "升降柱信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        lift_info_panel.add(panel6, gbc);
        height_feedback_label = new JLabel();
        height_feedback_label.setMaximumSize(new Dimension(160, 30));
        height_feedback_label.setMinimumSize(new Dimension(160, 30));
        height_feedback_label.setPreferredSize(new Dimension(160, 30));
        height_feedback_label.setText("当前高度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(height_feedback_label, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel6.add(spacer12, gbc);
        height_feedback_field = new JLabel();
        height_feedback_field.setMaximumSize(new Dimension(100, 30));
        height_feedback_field.setMinimumSize(new Dimension(100, 30));
        height_feedback_field.setPreferredSize(new Dimension(100, 30));
        height_feedback_field.setText("200mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(height_feedback_field, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        lift_info_panel.add(spacer13, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        lift_info_panel.add(panel7, gbc);
        speed_feedback_label = new JLabel();
        speed_feedback_label.setMaximumSize(new Dimension(160, 30));
        speed_feedback_label.setMinimumSize(new Dimension(160, 30));
        speed_feedback_label.setPreferredSize(new Dimension(160, 30));
        speed_feedback_label.setText("当前速度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(speed_feedback_label, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel7.add(spacer14, gbc);
        speed_feedback_field = new JLabel();
        speed_feedback_field.setMaximumSize(new Dimension(100, 30));
        speed_feedback_field.setMinimumSize(new Dimension(100, 30));
        speed_feedback_field.setPreferredSize(new Dimension(100, 30));
        speed_feedback_field.setText("200mm/s");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(speed_feedback_field, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        lift_info_panel.add(spacer15, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        lift_info_panel.add(panel8, gbc);
        status_feedback_label = new JLabel();
        status_feedback_label.setMaximumSize(new Dimension(160, 30));
        status_feedback_label.setMinimumSize(new Dimension(160, 30));
        status_feedback_label.setPreferredSize(new Dimension(160, 30));
        status_feedback_label.setText("当前状态");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(status_feedback_label, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel8.add(spacer16, gbc);
        status_feedback_field = new JLabel();
        status_feedback_field.setMaximumSize(new Dimension(100, 30));
        status_feedback_field.setMinimumSize(new Dimension(100, 30));
        status_feedback_field.setPreferredSize(new Dimension(100, 30));
        status_feedback_field.setText("空闲中");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(status_feedback_field, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        lift_info_panel.add(spacer17, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        lift_info_panel.add(spacer18, gbc);
        servo_info_panel = new JPanel();
        servo_info_panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(servo_info_panel, gbc);
        servo_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "伺服信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        servo_info_panel.add(panel9, gbc);
        current_feedback_label = new JLabel();
        current_feedback_label.setMaximumSize(new Dimension(160, 30));
        current_feedback_label.setMinimumSize(new Dimension(160, 30));
        current_feedback_label.setPreferredSize(new Dimension(160, 30));
        current_feedback_label.setText("当前电流");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(current_feedback_label, gbc);
        final JPanel spacer19 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel9.add(spacer19, gbc);
        current_feedback_field = new JLabel();
        current_feedback_field.setMaximumSize(new Dimension(100, 30));
        current_feedback_field.setMinimumSize(new Dimension(100, 30));
        current_feedback_field.setPreferredSize(new Dimension(100, 30));
        current_feedback_field.setText("1.25A");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel9.add(current_feedback_field, gbc);
        final JPanel spacer20 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        servo_info_panel.add(spacer20, gbc);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        servo_info_panel.add(panel10, gbc);
        temperature_feedback_label = new JLabel();
        temperature_feedback_label.setMaximumSize(new Dimension(160, 30));
        temperature_feedback_label.setMinimumSize(new Dimension(160, 30));
        temperature_feedback_label.setPreferredSize(new Dimension(160, 30));
        temperature_feedback_label.setText("当前温度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(temperature_feedback_label, gbc);
        final JPanel spacer21 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel10.add(spacer21, gbc);
        temperature_feedback_field = new JLabel();
        temperature_feedback_field.setMaximumSize(new Dimension(100, 30));
        temperature_feedback_field.setMinimumSize(new Dimension(100, 30));
        temperature_feedback_field.setPreferredSize(new Dimension(100, 30));
        temperature_feedback_field.setText("200");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(temperature_feedback_field, gbc);
        final JPanel spacer22 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        servo_info_panel.add(spacer22, gbc);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        servo_info_panel.add(panel11, gbc);
        errorcode_feedback_label = new JLabel();
        errorcode_feedback_label.setMaximumSize(new Dimension(160, 30));
        errorcode_feedback_label.setMinimumSize(new Dimension(160, 30));
        errorcode_feedback_label.setPreferredSize(new Dimension(160, 30));
        errorcode_feedback_label.setText("故障码");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(errorcode_feedback_label, gbc);
        final JPanel spacer23 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel11.add(spacer23, gbc);
        errorcode_feedback_field = new JLabel();
        errorcode_feedback_field.setMaximumSize(new Dimension(100, 30));
        errorcode_feedback_field.setMinimumSize(new Dimension(100, 30));
        errorcode_feedback_field.setPreferredSize(new Dimension(100, 30));
        errorcode_feedback_field.setText("无");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(errorcode_feedback_field, gbc);
        final JPanel spacer24 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        servo_info_panel.add(spacer24, gbc);
        final JPanel spacer25 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        servo_info_panel.add(spacer25, gbc);
        final JPanel spacer26 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        root.add(spacer26, gbc);
        final JPanel spacer27 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        root.add(spacer27, gbc);
        final JPanel spacer28 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        root.add(spacer28, gbc);
        logo = new JLabel();
        logo.setIcon(new ImageIcon(getClass().getResource("/images/logo.png")));
        logo.setText("");
        logo.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        root.add(logo, gbc);
        stroke_panel = new JPanel();
        stroke_panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        root.add(stroke_panel, gbc);
        stroke_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "行程", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        stroke_panel.add(panel12, gbc);
        stroke_label = new JLabel();
        stroke_label.setMaximumSize(new Dimension(100, 30));
        stroke_label.setMinimumSize(new Dimension(100, 30));
        stroke_label.setPreferredSize(new Dimension(100, 30));
        stroke_label.setText("Stroke:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel12.add(stroke_label, gbc);
        stroke_box = new JComboBox();
        stroke_box.setMaximumSize(new Dimension(100, 30));
        stroke_box.setMinimumSize(new Dimension(100, 30));
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("500");
        defaultComboBoxModel1.addElement("600");
        defaultComboBoxModel1.addElement("700");
        stroke_box.setModel(defaultComboBoxModel1);
        stroke_box.setPreferredSize(new Dimension(100, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel12.add(stroke_box, gbc);
        final JPanel spacer29 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel12.add(spacer29, gbc);
        final JPanel spacer30 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel12.add(spacer30, gbc);
        final JLabel label3 = new JLabel();
        label3.setMaximumSize(new Dimension(60, 30));
        label3.setMinimumSize(new Dimension(60, 30));
        label3.setPreferredSize(new Dimension(60, 30));
        label3.setText("mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel12.add(label3, gbc);
        final JPanel spacer31 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        stroke_panel.add(spacer31, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    public void showPort(Integer port) {
        this.port_field.setText(String.valueOf(port));
    }

    public void showLowVirtualLimit(Integer lowVirtualLimit) {
        this.low_virtual_limit_field.setText(String.valueOf(lowVirtualLimit));
    }

    public void showHighVirtualLimit(Integer highVirtualLimit) {
        this.high_virtual_limit_field.setText(String.valueOf(highVirtualLimit));
    }

    public void refreshState(boolean connectionStatus, Double height, Double speed, Double status, Double current, Double temperature, Double errorCode) {
        if (connectionStatus) {
            connect_status_label.setText(contribution.getTextResource().connected());

            height_feedback_field.setText(height + "mm");
            speed_feedback_field.setText(speed + "mm/s");
            //status_feedback_field.setText(String.valueOf(status));
            if (status == 1) {
                status_feedback_field.setText(contribution.getTextResource().moving());
            } else {
                status_feedback_field.setText(contribution.getTextResource().stop());
            }

            current_feedback_field.setText(current + "A");
            temperature_feedback_field.setText(String.valueOf(temperature));
            errorcode_feedback_field.setText(String.valueOf(errorCode));
        } else {
            connect_status_label.setText(contribution.getTextResource().disconnected());
            height_feedback_field.setText("--");
            speed_feedback_field.setText("--");
            status_feedback_field.setText("--");

            current_feedback_field.setText("--");
            temperature_feedback_field.setText("--");
            errorcode_feedback_field.setText("--");
        }
    }
}
