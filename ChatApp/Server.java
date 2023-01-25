import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

class Server extends JFrame{


    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;



     // Declare Components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messagArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);


    //Constructor
    public Server(){

        try {
            // System.out.println(socket.getPort());
            server=new ServerSocket(7777);
            System.out.println("Server is Ready to accept connection");
            System.out.println("Waiting");
            socket=server.accept();

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream()); 
             CreateGUI();
              handleEvents();


            startReading();
            // startWriting();



        } catch (Exception e) {
           e.printStackTrace();
        }
        
    }
        private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == 10) {
                    // System.out.println("You Press Enter");
                    String contentToSend = messageInput.getText();
                    messagArea.append("Me:" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });
    }
      // GUI COde
    private void CreateGUI() {
        this.setTitle("Server END");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Components Code

        heading.setFont(font);
        messagArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        messagArea.setEditable(false);

        // Frame-Layout

        this.setLayout(new BorderLayout());

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messagArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

    }
    public void startReading(){
        // Thread for Readin Data

        Runnable r1=()->{
            System.out.println("Reader Start");

            try {
                
             

            while(true){
                
        
                String msg=br.readLine();
                
                if(msg.equals("exit")){
                    System.out.println("Client Termintaed Chat");
                    JOptionPane.showMessageDialog(this, "Client Terminated the Chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                    
                }
                // System.out.println("Client: "+msg);
                 messagArea.append("Client: " + msg + "\n");
                

           
            
            }
        }catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Connection Closed");
        }

        
        };
    
        new Thread(r1).start();

    }
    public void startWriting(){
        // Thread take data from  user and send it to client

        Runnable r2=()->{
            System.out.println("Writter Started");
            try {
                
          
            while(true && !socket.isClosed()){

                    BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                   
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                    
               
            }
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Connection Closed");
        }
        };
        new Thread(r2).start();
        

    }


    public static void main(String[] args) {
        System.out.println("Server Started");
        new Server();
    }
}