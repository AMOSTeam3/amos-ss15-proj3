/*
 * This file is part of ReqTracker.
 *
 * Copyright (C) 2015 Taleh Didover, Florian Gerdes, Dmitry Gorelenkov,
 *     Rajab Hassan Kaoneka, Katsiaryna Krauchanka, Tobias Polzer,
 *     Gayathery Sathya, Lukas Tajak
 *
 * ReqTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ReqTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with ReqTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fau.osr.gui.util;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import de.fau.osr.util.ProgressBarInterface;

import java.awt.*;

/**
 * This class is a generic progress bar that can be utilized for any progress bar functionality
 * @author Gayathery Sathya
 */
public class SpiceTraceabilityProgressBar extends JFrame implements ProgressBarInterface {

    private JPanel contentPane;
    private JProgressBar progressBar;
    private JLabel lblPercentage;
    private JLabel lblProgress;

    /**
     * Create the frame.
     */
    public SpiceTraceabilityProgressBar() {
        setType(Type.UTILITY);
        setResizable(false);
        setTitle("ReqTracker : Progress");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 442, 160);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        progressBar = new JProgressBar();

        lblProgress = new JLabel("Progress...");
        lblProgress.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblProgress.setForeground(Color.BLUE);

        lblPercentage = new JLabel("0%");
        lblPercentage.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblPercentage.setForeground(new Color(205, 92, 92));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addComponent(lblProgress)
                            .addGap(34)
                            .addComponent(lblPercentage))
                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE))
                    .addContainerGap())
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.TRAILING)
                .addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblProgress)
                        .addComponent(lblPercentage))
                    .addGap(18)
                    .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(34, Short.MAX_VALUE))
        );
        contentPane.setLayout(gl_contentPane);
        pack();
    }

    /**
     * @param percentage value of the progress
     */
    public void setProgressBarValue(int percentage){
    	EventQueue.invokeLater(() -> {
    		progressBar.setValue(percentage);
        	lblPercentage.setText(""+percentage+"%");
        });
    	if(percentage == 100) {
    		this.dispose();
    	}
    }

    /**
     * @param content string data to show while progress bar is progressing
     */
    public void setProgressBarContent(String content){
    	lblProgress.setText(content + "...");
    }

}
