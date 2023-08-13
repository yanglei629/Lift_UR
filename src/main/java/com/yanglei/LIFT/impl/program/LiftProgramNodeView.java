package com.yanglei.LIFT.impl.program;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.yanglei.LIFT.impl.Style;
import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardNumberInput;


public class LiftProgramNodeView implements SwingProgramNodeView<LiftProgramNodeContribution> {

    private Style style;
    private JSlider posSlider = new JSlider(JSlider.VERTICAL);
    private static final String filePath_logo = "/logo/logo_backyard.jpg";
    private JTextField inputPos;
    private JLabel labelTargetPos;
    private JLabel labelCurrentPos;
    private JLabel labelMovingStatus;
    private JButton performBtn;
    private JLabel targetPosLabel;
    private JLabel statusHeader;
    private JLabel argumentHeader;
    private JButton stop_btn;
    private JLabel labelConnectionStatus;
    private JCheckBox autoConnectCheckBox;
    private JPanel root;
    private JButton execute_btn;
    private JSlider target_height_slider;
    private JTextField target_height_field;
    private JSlider target_speed_slider;
    private JTextField target_speed_field;

    public LiftProgramNodeView(Style style) {
        this.style = style;
    }

    public LiftProgramNodeView() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LiftProgramNodeView");
        frame.setContentPane(new LiftProgramNodeView().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void buildUI(JPanel panel, ContributionProvider<LiftProgramNodeContribution> provider) {
        //LiftProgramNodeContribution contribution = provider.get();
        /*panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(createInfo());

        panel.add(createPosSlider(posSlider, 0, 300, provider));
        panel.add(style.createVerticalSpacing());
        panel.add(style.createVerticalSpacing());
        panel.add(createStatusBox(provider));
        panel.add(style.createVerticalSpacing());
        panel.add(style.createVerticalSpacing());
        Box imagebox = Box.createHorizontalBox();
        imagebox.setAlignmentX(Component.LEFT_ALIGNMENT);
        ImageIcon icon1 = new ImageIcon(getScaledImage(getImage(filePath_logo), 200, 96));
        JLabel labelLogo1 = new JLabel();
        labelLogo1.setIcon(icon1);
        imagebox.add(Box.createHorizontalGlue());
        imagebox.add(style.createHorizontalSpacing());
        imagebox.add(labelLogo1);
        panel.add(imagebox);*/

        panel.setLayout(new BorderLayout());
        panel.add($$$getRootComponent$$$());
        setUI(provider);
    }

    private void setUI(final ContributionProvider<LiftProgramNodeContribution> provider) {
        // i18n

        // set listener
        target_height_slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                target_height_field.setText(String.valueOf(target_height_slider.getValue()));
                provider.get().setPos(target_height_slider.getValue());
            }
        });

        target_height_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                KeyboardNumberInput<Integer> keyboardInput = provider.get().getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(provider.get().getPos());
                keyboardInput.show(target_height_field, new KeyboardInputCallback<Integer>() {
                    @Override
                    public void onOk(Integer value) {
                        target_height_field.setText(String.valueOf(value));
                        target_height_slider.setValue(value);
                        provider.get().setPos(value);
                    }
                });
            }
        });

        target_speed_slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                target_speed_field.setText(String.valueOf(target_speed_slider.getValue()));
                provider.get().setSpeed(target_speed_slider.getValue());
            }
        });

        target_speed_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                KeyboardNumberInput<Integer> keyboardInput = provider.get().getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(provider.get().getSpeed());
                keyboardInput.show(target_speed_field, new KeyboardInputCallback<Integer>() {
                    @Override
                    public void onOk(Integer value) {
                        target_speed_field.setText(String.valueOf(value));
                        target_speed_slider.setValue(value);
                        provider.get().setSpeed(value);
                    }
                });
            }
        });

        execute_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                provider.get().execute();
            }
        });
    }

    private Box createStatusBox(ContributionProvider<LiftProgramNodeContribution> provider) {
        Box box = Box.createVerticalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusHeader = new JLabel("Status");
        statusHeader.setFont(new Font("宋体bai", Font.BOLD, 15));
        statusHeader.setFont(statusHeader.getFont().deriveFont(Font.BOLD, style.getSmallHeaderFontSize()));

        box.add(statusHeader);
        labelConnectionStatus = new JLabel("Connection Status");
        labelCurrentPos = new JLabel("Current Position:");
        labelTargetPos = new JLabel("Target Position:");
        labelMovingStatus = new JLabel("Running Status:");
        box.add(style.createVerticalSpacing());
        box.add(labelConnectionStatus);
        box.add(Box.createVerticalStrut(10));
        box.add(labelCurrentPos);
        box.add(Box.createVerticalStrut(10));
        box.add(labelTargetPos);
        box.add(Box.createVerticalStrut(10));
        box.add(labelMovingStatus);
        box.add(Box.createVerticalStrut(10));

        return box;
    }


    private Box createInfo() {
        Box box = Box.createHorizontalBox();
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        //----------------------------------------------
        Box infoBox = Box.createVerticalBox();
        infoBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        argumentHeader = new JLabel("Action Arguments");
        argumentHeader.setFont(new Font("宋体bai", Font.BOLD, 15));
        argumentHeader.setFont(argumentHeader.getFont().deriveFont(Font.BOLD, style.getSmallHeaderFontSize()));


        infoBox.add(argumentHeader);
        infoBox.add(Box.createVerticalStrut(10));
        Box logoBox = Box.createHorizontalBox();
        logoBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon1 = new ImageIcon(getScaledImage(getImage(filePath_logo), 200, 96));
        JLabel labelLogo1 = new JLabel();
        labelLogo1.setIcon(icon1);
        box.add(infoBox);
        return box;
    }

    public void updateTextField(int value) {
        inputPos.setText(Integer.toString(value));
    }

    public void setPosSlider(int value) {
        posSlider.setValue(value);
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

    public void showPos(int value) {
        target_height_field.setText(Integer.toString(value));
    }

    public void setTargetPosLabel(String text) {
        labelTargetPos.setText(text);
    }

    public void setCurrentPosLabel(String text) {
        labelCurrentPos.setText(text);
    }

    public void setMovingStatus(String text) {
        labelMovingStatus.setText(text);
    }

    public void setPerformBtn(String perform) {
        performBtn.setText(perform);
    }

    public void setTargetPos(String text) {
        targetPosLabel.setText(text);
    }

    public void setStatusText(String status) {
        statusHeader.setText(status);
    }

    public void setArgumentText(String argument) {
        argumentHeader.setText(argument);
    }

    public void setStopBtn(String stop) {
        stop_btn.setText(stop);
    }

    public void setConnectionStatus(String s) {
        labelConnectionStatus.setText(s);
    }

    public void showAutoActivate(boolean autoActivation) {
        autoConnectCheckBox.setSelected(autoActivation);
    }

    public void setAutoConnection(String autoActivation) {
        autoConnectCheckBox.setText(autoActivation);
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(panel1, gbc);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "移动设置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        final JLabel label1 = new JLabel();
        label1.setMaximumSize(new Dimension(80, 30));
        label1.setMinimumSize(new Dimension(80, 30));
        label1.setPreferredSize(new Dimension(80, 30));
        label1.setText("目标高度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label1, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel2.add(spacer1, gbc);
        target_height_slider = new JSlider();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(target_height_slider, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel2.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(spacer3, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel3, gbc);
        final JLabel label2 = new JLabel();
        label2.setMaximumSize(new Dimension(80, 30));
        label2.setMinimumSize(new Dimension(80, 30));
        label2.setPreferredSize(new Dimension(80, 30));
        label2.setText("目标速度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label2, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel3.add(spacer4, gbc);
        target_speed_slider = new JSlider();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(target_speed_slider, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel3.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 5;
        panel1.add(spacer6, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel1.add(panel4, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel4.add(spacer7, gbc);
        execute_btn = new JButton();
        execute_btn.setMaximumSize(new Dimension(100, 30));
        execute_btn.setMinimumSize(new Dimension(100, 30));
        execute_btn.setPreferredSize(new Dimension(100, 30));
        execute_btn.setText("试运行");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(execute_btn, gbc);
        stop_btn = new JButton();
        stop_btn.setMaximumSize(new Dimension(100, 30));
        stop_btn.setMinimumSize(new Dimension(100, 30));
        stop_btn.setPreferredSize(new Dimension(100, 30));
        stop_btn.setText("停止");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(stop_btn, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel1.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel1.add(spacer9, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label3, gbc);
        target_height_field = new JTextField();
        target_height_field.setMaximumSize(new Dimension(60, 30));
        target_height_field.setMinimumSize(new Dimension(60, 30));
        target_height_field.setPreferredSize(new Dimension(60, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(target_height_field, gbc);
        target_speed_field = new JTextField();
        target_speed_field.setMaximumSize(new Dimension(60, 30));
        target_speed_field.setMinimumSize(new Dimension(60, 30));
        target_speed_field.setPreferredSize(new Dimension(60, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(target_speed_field, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("mm/s");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(label4, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 2;
        panel1.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 2;
        panel1.add(spacer11, gbc);
        final JPanel spacer12 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        root.add(spacer12, gbc);
        final JPanel spacer13 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        root.add(spacer13, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(panel5, gbc);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "升降柱信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(panel6, gbc);
        final JLabel label5 = new JLabel();
        label5.setMaximumSize(new Dimension(80, 30));
        label5.setMinimumSize(new Dimension(80, 30));
        label5.setPreferredSize(new Dimension(80, 30));
        label5.setText("当前高度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(label5, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel6.add(spacer14, gbc);
        final JLabel label6 = new JLabel();
        label6.setMaximumSize(new Dimension(100, 30));
        label6.setMinimumSize(new Dimension(100, 30));
        label6.setPreferredSize(new Dimension(100, 30));
        label6.setText("200mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(label6, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(spacer15, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(panel7, gbc);
        final JLabel label7 = new JLabel();
        label7.setMaximumSize(new Dimension(80, 30));
        label7.setMinimumSize(new Dimension(80, 30));
        label7.setPreferredSize(new Dimension(80, 30));
        label7.setText("当前速度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(label7, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel7.add(spacer16, gbc);
        final JLabel label8 = new JLabel();
        label8.setMaximumSize(new Dimension(100, 30));
        label8.setMinimumSize(new Dimension(100, 30));
        label8.setPreferredSize(new Dimension(100, 30));
        label8.setText("200mm/s");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel7.add(label8, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel5.add(spacer17, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(panel8, gbc);
        final JLabel label9 = new JLabel();
        label9.setMaximumSize(new Dimension(80, 30));
        label9.setMinimumSize(new Dimension(80, 30));
        label9.setPreferredSize(new Dimension(80, 30));
        label9.setText("当前状态");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label9, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel8.add(spacer18, gbc);
        final JLabel label10 = new JLabel();
        label10.setMaximumSize(new Dimension(100, 30));
        label10.setMinimumSize(new Dimension(100, 30));
        label10.setPreferredSize(new Dimension(100, 30));
        label10.setText("空闲中");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label10, gbc);
        final JPanel spacer19 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel5.add(spacer19, gbc);
        final JPanel spacer20 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        panel5.add(spacer20, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    public void showSpeed(Integer speed) {
        this.target_speed_field.setText(String.valueOf(speed));
    }
}
