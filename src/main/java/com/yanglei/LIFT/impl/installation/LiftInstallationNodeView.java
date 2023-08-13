package com.yanglei.LIFT.impl.installation;

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
    private JTextField stroke_field;
    private JButton connect_btn;
    private JCheckBox low_virtual_limit_checkbox;
    private JTextField low_virtual_limit_field;
    private JTextField high_virtual_limit_field;
    private JButton reset_btn;
    private JLabel connect_status_label;
    private JCheckBox high_virtual_limit_checkbox;

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

        // set listener
        ip_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
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
                KeyboardNumberInput<Integer> keyboardInput = contribution.getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(contribution.getPort());
                keyboardInput.show(port_field, new KeyboardInputCallback<Integer>() {
                    @Override
                    public void onOk(Integer value) {
                        port_field.setText(String.valueOf(value));
                        contribution.setPort(value);
                    }
                });
            }
        });

        low_virtual_limit_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
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
                KeyboardNumberInput<Integer> keyboardInput = contribution.getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(contribution.getHighVirtualLimit());
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

    public void setConnected() {
        setConnectStatusLabel(this.contribution.getTextResource().connected());
        modeBox.setVisible(true);
        controlBox.setVisible(true);
    }

    public void setStopBtn(String stop) {
        stopBtn.setText(stop);
    }

    public void setCancelStopBtn(String cancelStop) {
        cancelStopBtn.setText(cancelStop);
    }


    /**
     * lift status
     */
    public void setStatusText(String status) {
        statusHeader.setText(status);
    }

    public void setCurrentPosLabel(String text) {
        labelCurrentPos.setText(text);
    }

    public void setMovingStatus(String text) {
        labelMovingStatus.setText(text);
    }

    public void setModeBox(Integer mode) {
        if (mode == 10) {
            modeComboBox.setSelectedIndex(0);
        } else {
            modeComboBox.setSelectedIndex(1);
        }
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setBackground(new Color(-1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "连接信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(panel2, gbc);
        final JLabel label1 = new JLabel();
        label1.setMaximumSize(new Dimension(50, 30));
        label1.setMinimumSize(new Dimension(50, 30));
        label1.setPreferredSize(new Dimension(50, 30));
        label1.setText("IP:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label1, gbc);
        ip_field = new JTextField();
        ip_field.setMaximumSize(new Dimension(80, 30));
        ip_field.setMinimumSize(new Dimension(80, 30));
        ip_field.setPreferredSize(new Dimension(120, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(ip_field, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel2.add(spacer1, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(panel3, gbc);
        final JLabel label2 = new JLabel();
        label2.setMaximumSize(new Dimension(50, 30));
        label2.setMinimumSize(new Dimension(50, 30));
        label2.setText("Port:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label2, gbc);
        port_field = new JTextField();
        port_field.setMaximumSize(new Dimension(100, 30));
        port_field.setMinimumSize(new Dimension(100, 30));
        port_field.setPreferredSize(new Dimension(100, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(port_field, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(panel4, gbc);
        final JLabel label3 = new JLabel();
        label3.setMaximumSize(new Dimension(50, 30));
        label3.setMinimumSize(new Dimension(50, 30));
        label3.setText("Stroke:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label3, gbc);
        stroke_field = new JTextField();
        stroke_field.setMaximumSize(new Dimension(100, 30));
        stroke_field.setMinimumSize(new Dimension(100, 30));
        stroke_field.setPreferredSize(new Dimension(100, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(stroke_field, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(panel5, gbc);
        connect_btn = new JButton();
        connect_btn.setMaximumSize(new Dimension(100, 30));
        connect_btn.setMinimumSize(new Dimension(100, 30));
        connect_btn.setPreferredSize(new Dimension(100, 30));
        connect_btn.setText("连接");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(connect_btn, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 3;
        panel5.add(spacer2, gbc);
        connect_status_label = new JLabel();
        connect_status_label.setMaximumSize(new Dimension(120, 30));
        connect_status_label.setMinimumSize(new Dimension(120, 30));
        connect_status_label.setPreferredSize(new Dimension(120, 30));
        connect_status_label.setText("未连接");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(connect_status_label, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel5.add(spacer3, gbc);
        reset_btn = new JButton();
        reset_btn.setMaximumSize(new Dimension(100, 30));
        reset_btn.setMinimumSize(new Dimension(100, 30));
        reset_btn.setPreferredSize(new Dimension(100, 30));
        reset_btn.setText("复位");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(reset_btn, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel5.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel1.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel1.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel1.add(spacer7, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(panel6, gbc);
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "虚拟限位", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(panel7, gbc);
        low_virtual_limit_checkbox = new JCheckBox();
        low_virtual_limit_checkbox.setMaximumSize(new Dimension(150, 30));
        low_virtual_limit_checkbox.setMinimumSize(new Dimension(150, 30));
        low_virtual_limit_checkbox.setPreferredSize(new Dimension(150, 30));
        low_virtual_limit_checkbox.setText("虚拟最低限位");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(low_virtual_limit_checkbox, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(spacer8, gbc);
        low_virtual_limit_field = new JTextField();
        low_virtual_limit_field.setMaximumSize(new Dimension(80, 30));
        low_virtual_limit_field.setMinimumSize(new Dimension(80, 30));
        low_virtual_limit_field.setPreferredSize(new Dimension(80, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(low_virtual_limit_field, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel6.add(spacer9, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(panel8, gbc);
        high_virtual_limit_checkbox = new JCheckBox();
        high_virtual_limit_checkbox.setMaximumSize(new Dimension(150, 30));
        high_virtual_limit_checkbox.setMinimumSize(new Dimension(150, 30));
        high_virtual_limit_checkbox.setPreferredSize(new Dimension(150, 30));
        high_virtual_limit_checkbox.setText("虚拟最高限位");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(high_virtual_limit_checkbox, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(spacer10, gbc);
        high_virtual_limit_field = new JTextField();
        high_virtual_limit_field.setMaximumSize(new Dimension(80, 30));
        high_virtual_limit_field.setMinimumSize(new Dimension(80, 30));
        high_virtual_limit_field.setPreferredSize(new Dimension(80, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(high_virtual_limit_field, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel8.add(spacer11, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel6.add(spacer12, gbc);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(panel9, gbc);
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "升降柱信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(panel10, gbc);
        final JLabel label4 = new JLabel();
        label4.setMaximumSize(new Dimension(80, 30));
        label4.setMinimumSize(new Dimension(80, 30));
        label4.setPreferredSize(new Dimension(80, 30));
        label4.setText("当前高度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(label4, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel10.add(spacer13, gbc);
        final JLabel label5 = new JLabel();
        label5.setMaximumSize(new Dimension(100, 30));
        label5.setMinimumSize(new Dimension(100, 30));
        label5.setPreferredSize(new Dimension(100, 30));
        label5.setText("200mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel10.add(label5, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel9.add(spacer14, gbc);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(panel11, gbc);
        final JLabel label6 = new JLabel();
        label6.setMaximumSize(new Dimension(80, 30));
        label6.setMinimumSize(new Dimension(80, 30));
        label6.setPreferredSize(new Dimension(80, 30));
        label6.setText("当前速度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(label6, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel11.add(spacer15, gbc);
        final JLabel label7 = new JLabel();
        label7.setMaximumSize(new Dimension(100, 30));
        label7.setMinimumSize(new Dimension(100, 30));
        label7.setPreferredSize(new Dimension(100, 30));
        label7.setText("200mm/s");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel11.add(label7, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel9.add(spacer16, gbc);
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        panel9.add(panel12, gbc);
        final JLabel label8 = new JLabel();
        label8.setMaximumSize(new Dimension(80, 30));
        label8.setMinimumSize(new Dimension(80, 30));
        label8.setPreferredSize(new Dimension(80, 30));
        label8.setText("当前状态");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel12.add(label8, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel12.add(spacer17, gbc);
        final JLabel label9 = new JLabel();
        label9.setMaximumSize(new Dimension(100, 30));
        label9.setMinimumSize(new Dimension(100, 30));
        label9.setPreferredSize(new Dimension(100, 30));
        label9.setText("空闲中");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel12.add(label9, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel9.add(spacer18, gbc);
        final JPanel spacer19 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel9.add(spacer19, gbc);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(panel13, gbc);
        panel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "伺服信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel13.add(panel14, gbc);
        final JLabel label10 = new JLabel();
        label10.setMaximumSize(new Dimension(80, 30));
        label10.setMinimumSize(new Dimension(80, 30));
        label10.setPreferredSize(new Dimension(80, 30));
        label10.setText("当前电流");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel14.add(label10, gbc);
        final JPanel spacer20 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel14.add(spacer20, gbc);
        final JLabel label11 = new JLabel();
        label11.setMaximumSize(new Dimension(100, 30));
        label11.setMinimumSize(new Dimension(100, 30));
        label11.setPreferredSize(new Dimension(100, 30));
        label11.setText("1.25A");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel14.add(label11, gbc);
        final JPanel spacer21 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel13.add(spacer21, gbc);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel13.add(panel15, gbc);
        final JLabel label12 = new JLabel();
        label12.setMaximumSize(new Dimension(80, 30));
        label12.setMinimumSize(new Dimension(80, 30));
        label12.setPreferredSize(new Dimension(80, 30));
        label12.setText("当前温度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel15.add(label12, gbc);
        final JPanel spacer22 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel15.add(spacer22, gbc);
        final JLabel label13 = new JLabel();
        label13.setMaximumSize(new Dimension(100, 30));
        label13.setMinimumSize(new Dimension(100, 30));
        label13.setPreferredSize(new Dimension(100, 30));
        label13.setText("200mm/s");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel15.add(label13, gbc);
        final JPanel spacer23 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel13.add(spacer23, gbc);
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        panel13.add(panel16, gbc);
        final JLabel label14 = new JLabel();
        label14.setMaximumSize(new Dimension(80, 30));
        label14.setMinimumSize(new Dimension(80, 30));
        label14.setPreferredSize(new Dimension(80, 30));
        label14.setText("故障码");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel16.add(label14, gbc);
        final JPanel spacer24 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel16.add(spacer24, gbc);
        final JLabel label15 = new JLabel();
        label15.setMaximumSize(new Dimension(100, 30));
        label15.setMinimumSize(new Dimension(100, 30));
        label15.setPreferredSize(new Dimension(100, 30));
        label15.setText("无");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel16.add(label15, gbc);
        final JPanel spacer25 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel13.add(spacer25, gbc);
        final JPanel spacer26 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel13.add(spacer26, gbc);
        final JPanel spacer27 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        root.add(spacer27, gbc);
        final JPanel spacer28 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        root.add(spacer28, gbc);
        final JPanel spacer29 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 3;
        root.add(spacer29, gbc);
        final JLabel label16 = new JLabel();
        label16.setIcon(new ImageIcon(getClass().getResource("/images/logo.png")));
        label16.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        root.add(label16, gbc);
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
}
