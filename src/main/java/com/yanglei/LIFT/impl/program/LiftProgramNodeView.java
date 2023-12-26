package com.yanglei.LIFT.impl.program;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidator;
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
    private JLabel height_feedback_field;
    private JLabel speed_feedback_field;
    private JLabel status_feedback_field;
    private JLabel target_height_label;
    private JLabel target_speed_label;
    private JLabel height_feedback_label;
    private JLabel speed_feedback_label;
    private JLabel status_feedback_label;
    private JPanel lift_info_panel;
    private JPanel move_panel;
    private JButton reset_btn;
    private JButton jog_up_btn;
    private JButton jog_down_btn;

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
        panel.setLayout(new BorderLayout());
        panel.add($$$getRootComponent$$$());
        setUI(provider);
    }

    private void setUI(final ContributionProvider<LiftProgramNodeContribution> provider) {
        /*// i18n
        target_height_label.setText(provider.get().getTextResource().targetPos());
        target_speed_label.setText(provider.get().getTextResource().targetSpeed());
        execute_btn.setText(provider.get().getTextResource().perform());
        stop_btn.setText(provider.get().getTextResource().stop());*/
        //lift_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), provider.get().getTextResource().liftInfo(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        //servo_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), this.contribution.getTextResource().servoInfo(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));


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
                if (!provider.get().getInstalltion().isConnect()) {
                    return;
                }
                KeyboardNumberInput<Integer> keyboardInput = provider.get().getKeyboardFactory().createIntegerKeypadInput();
                keyboardInput.setInitialValue(provider.get().getPos());
                //Integer min = 0;
                Integer min = provider.get().getInstalltion().getLowVirtualLimit();
                //Integer max = provider.get().getInstalltion().getStroke();
                Integer max = provider.get().getInstalltion().getHighVirtualLimit();
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
                if (!provider.get().getInstalltion().isConnect()) {
                    return;
                }
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

        stop_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                provider.get().stopLift();
            }
        });

        reset_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);
                provider.get().reset();
            }
        });

        jog_up_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                provider.get().jogUp(true);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
                provider.get().jogUp(false);
                ArrayList<Double> liftingInfo = provider.get().getInstalltion().getLiftingInfo();
                Integer current_pos = liftingInfo.get(0).intValue();
                Integer current_speed = liftingInfo.get(1).intValue();
                provider.get().setPos(current_pos);
                target_height_slider.setValue(current_pos);
                target_height_field.setText(String.valueOf(current_pos));
            }
        });

        jog_down_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                provider.get().jogDown(true);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
                provider.get().jogDown(false);
                ArrayList<Double> liftingInfo = provider.get().getInstalltion().getLiftingInfo();
                Integer current_pos = liftingInfo.get(0).intValue();
                Integer current_speed = liftingInfo.get(1).intValue();
                provider.get().setPos(current_pos);
                target_height_slider.setValue(current_pos);
                target_height_field.setText(String.valueOf(current_pos));
            }
        });
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
        target_height_slider.setValue(value);
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
        move_panel = new JPanel();
        move_panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(move_panel, gbc);
        move_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "移动设置", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        move_panel.add(panel1, gbc);
        target_height_label = new JLabel();
        target_height_label.setMaximumSize(new Dimension(120, 30));
        target_height_label.setMinimumSize(new Dimension(120, 30));
        target_height_label.setPreferredSize(new Dimension(120, 30));
        target_height_label.setText("目标高度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel1.add(target_height_label, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel1.add(spacer1, gbc);
        target_height_slider = new JSlider();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(target_height_slider, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel1.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        move_panel.add(spacer3, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        move_panel.add(panel2, gbc);
        target_speed_label = new JLabel();
        target_speed_label.setMaximumSize(new Dimension(120, 30));
        target_speed_label.setMinimumSize(new Dimension(120, 30));
        target_speed_label.setPreferredSize(new Dimension(120, 30));
        target_speed_label.setText("目标速度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(target_speed_label, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel2.add(spacer4, gbc);
        target_speed_slider = new JSlider();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(target_speed_slider, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel2.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 5;
        move_panel.add(spacer6, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        move_panel.add(panel3, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel3.add(spacer7, gbc);
        execute_btn = new JButton();
        execute_btn.setMaximumSize(new Dimension(180, 30));
        execute_btn.setMinimumSize(new Dimension(180, 30));
        execute_btn.setPreferredSize(new Dimension(180, 30));
        execute_btn.setText("试运行");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(execute_btn, gbc);
        stop_btn = new JButton();
        stop_btn.setMaximumSize(new Dimension(100, 30));
        stop_btn.setMinimumSize(new Dimension(100, 30));
        stop_btn.setPreferredSize(new Dimension(100, 30));
        stop_btn.setText("停止");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(stop_btn, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        move_panel.add(spacer8, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        move_panel.add(spacer9, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        move_panel.add(label1, gbc);
        target_height_field = new JTextField();
        target_height_field.setMaximumSize(new Dimension(60, 30));
        target_height_field.setMinimumSize(new Dimension(60, 30));
        target_height_field.setPreferredSize(new Dimension(60, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        move_panel.add(target_height_field, gbc);
        target_speed_field = new JTextField();
        target_speed_field.setMaximumSize(new Dimension(60, 30));
        target_speed_field.setMinimumSize(new Dimension(60, 30));
        target_speed_field.setPreferredSize(new Dimension(60, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        move_panel.add(target_speed_field, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("mm/s");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        move_panel.add(label2, gbc);
        final JPanel spacer10 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 2;
        move_panel.add(spacer10, gbc);
        final JPanel spacer11 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 2;
        move_panel.add(spacer11, gbc);
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
        lift_info_panel = new JPanel();
        lift_info_panel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        root.add(lift_info_panel, gbc);
        lift_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "升降柱信息", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        lift_info_panel.add(panel4, gbc);
        height_feedback_label = new JLabel();
        height_feedback_label.setMaximumSize(new Dimension(150, 30));
        height_feedback_label.setMinimumSize(new Dimension(150, 30));
        height_feedback_label.setPreferredSize(new Dimension(150, 30));
        height_feedback_label.setText("当前高度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(height_feedback_label, gbc);
        final JPanel spacer14 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel4.add(spacer14, gbc);
        height_feedback_field = new JLabel();
        height_feedback_field.setMaximumSize(new Dimension(100, 30));
        height_feedback_field.setMinimumSize(new Dimension(100, 30));
        height_feedback_field.setPreferredSize(new Dimension(100, 30));
        height_feedback_field.setText("200mm");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(height_feedback_field, gbc);
        final JPanel spacer15 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        lift_info_panel.add(spacer15, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        lift_info_panel.add(panel5, gbc);
        speed_feedback_label = new JLabel();
        speed_feedback_label.setMaximumSize(new Dimension(150, 30));
        speed_feedback_label.setMinimumSize(new Dimension(150, 30));
        speed_feedback_label.setPreferredSize(new Dimension(150, 30));
        speed_feedback_label.setText("当前速度");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(speed_feedback_label, gbc);
        final JPanel spacer16 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel5.add(spacer16, gbc);
        speed_feedback_field = new JLabel();
        speed_feedback_field.setMaximumSize(new Dimension(100, 30));
        speed_feedback_field.setMinimumSize(new Dimension(100, 30));
        speed_feedback_field.setPreferredSize(new Dimension(100, 30));
        speed_feedback_field.setText("200mm/s");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(speed_feedback_field, gbc);
        final JPanel spacer17 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.VERTICAL;
        lift_info_panel.add(spacer17, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        lift_info_panel.add(panel6, gbc);
        status_feedback_label = new JLabel();
        status_feedback_label.setMaximumSize(new Dimension(150, 30));
        status_feedback_label.setMinimumSize(new Dimension(150, 30));
        status_feedback_label.setPreferredSize(new Dimension(150, 30));
        status_feedback_label.setText("当前状态");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(status_feedback_label, gbc);
        final JPanel spacer18 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 5;
        panel6.add(spacer18, gbc);
        status_feedback_field = new JLabel();
        status_feedback_field.setMaximumSize(new Dimension(100, 30));
        status_feedback_field.setMinimumSize(new Dimension(100, 30));
        status_feedback_field.setPreferredSize(new Dimension(100, 30));
        status_feedback_field.setText("空闲中");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel6.add(status_feedback_field, gbc);
        final JPanel spacer19 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        lift_info_panel.add(spacer19, gbc);
        final JPanel spacer20 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 1;
        lift_info_panel.add(spacer20, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        lift_info_panel.add(panel7, gbc);
        reset_btn = new JButton();
        reset_btn.setMaximumSize(new Dimension(130, 30));
        reset_btn.setMinimumSize(new Dimension(130, 30));
        reset_btn.setPreferredSize(new Dimension(130, 30));
        reset_btn.setText("重置");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(reset_btn, gbc);
        final JPanel spacer21 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(spacer21, gbc);
        jog_up_btn = new JButton();
        jog_up_btn.setMaximumSize(new Dimension(130, 30));
        jog_up_btn.setMinimumSize(new Dimension(130, 30));
        jog_up_btn.setPreferredSize(new Dimension(130, 30));
        jog_up_btn.setText("上升");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(jog_up_btn, gbc);
        jog_down_btn = new JButton();
        jog_down_btn.setMaximumSize(new Dimension(130, 30));
        jog_down_btn.setMinimumSize(new Dimension(130, 30));
        jog_down_btn.setPreferredSize(new Dimension(130, 30));
        jog_down_btn.setText("下降");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(jog_down_btn, gbc);
        final JPanel spacer22 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel7.add(spacer22, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

    public void showSpeed(Integer speed) {
        this.target_speed_slider.setValue(speed);
        this.target_speed_field.setText(String.valueOf(speed));
    }

    public void reDefineComponent(LiftProgramNodeContribution liftProgramNodeContribution) {
        target_height_slider.setMinimum(liftProgramNodeContribution.getInstalltion().getLowVirtualLimit());
        target_height_slider.setMaximum(liftProgramNodeContribution.getInstalltion().getHighVirtualLimit());
    }

    public void refreshState(LiftProgramNodeContribution liftProgramNodeContribution, boolean b, Double height, Double speed, Double status) {
        if (b) {
            height_feedback_field.setText(height + "mm");
            speed_feedback_field.setText(speed + "mm/s");
            status_feedback_field.setText(String.valueOf(status));
            if (status == 1) {
                status_feedback_field.setText(liftProgramNodeContribution.getTextResource().moving());
            } else {
                status_feedback_field.setText(liftProgramNodeContribution.getTextResource().stop());
            }
        } else {
            height_feedback_field.setText("--");
            speed_feedback_field.setText("--");
            status_feedback_field.setText("--");
        }
    }

    public void setExecuteBtn(String perform) {
        execute_btn.setText(perform);
    }

    public void setTargetSpeedLabel(String targetSpeed) {
        target_speed_label.setText(targetSpeed);
    }

    public void setTargetHeightLabel(String targetPos) {
        target_height_label.setText(targetPos);
    }

    public void setLiftInfoPanel(String liftInfo) {
        lift_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), liftInfo, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    }

    public void setMovePanel(String move) {
        move_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), move, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    }

    public void setResetBtn(String reset) {
        reset_btn.setText(reset);
    }

    public void setJogUpBtn(String jogUp) {
        jog_up_btn.setText(jogUp);
    }

    public void setJogDownBtn(String jogDown) {
        jog_down_btn.setText(jogDown);
    }

    public void setConnect(boolean connect) {
        target_height_field.setEnabled(connect);
        target_height_slider.setEnabled(connect);
        target_speed_field.setEnabled(connect);
        target_speed_slider.setEnabled(connect);

        execute_btn.setEnabled(connect);
        jog_up_btn.setEnabled(connect);
        jog_down_btn.setEnabled(connect);
        reset_btn.setEnabled(connect);
    }

    public void setHeightFeedbackLabel(String currentPos) {
        height_feedback_label.setText(currentPos);
    }

    public void setSpeedFeedbackLabel(String currentSpeed) {
        speed_feedback_label.setText(currentSpeed);
    }

    public void setStatusFeedbackLabel(String status) {
        status_feedback_label.setText(status);
    }
}
