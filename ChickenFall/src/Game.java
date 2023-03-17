
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author utku
 */
class Cat{
    private int x;
    private int y;

    public Cat() {
        x = (int) (Math.random() * 600 + 100);
        y = 750;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
class Egg{
    private int x;
    private int y;
    private final int direction; //-1 for left +1 for right

    public Egg(int x, int y,int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }
}
class Target{
    private int x;
    private int y;
    private final int size; // 1 2 3 (for point)

    public Target(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size * 20;
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
public class Game extends JPanel implements KeyListener,ActionListener{
    Timer timer = new Timer(10,this);
    private int score = 0;
    
    private BufferedImage image; //chicken
    private BufferedImage catImg;
    private ArrayList<Cat> cats = new ArrayList<Cat>();
    
    private BufferedImage startImg;
    private BufferedImage endImg;
    private BufferedImage level2Img;
    private BufferedImage level3Img;
    private BufferedImage tryagainImg;

    private int levelState = 0;
    private boolean isPlaying = false;
    // -1-gameover  0-beginning 1-level 1 2-level 2 3-level 3 4-end
    
    private ArrayList<Egg> eggs = new ArrayList<Egg>();
    private ArrayList<Target> targets = new ArrayList<Target>();
    
    private int eggSpeedDefault = 20;
    private int targetSpeedDefault = 5;
    private int catSpeedDefault = 5;
    private int eggSpeed = eggSpeedDefault;
    private int targetSpeed = targetSpeedDefault;
    private int catSpeed = catSpeedDefault;
    private int chickenX = 360; //başlangıç konumu
    
    private int dirChickenX = 20; //tavuk hareket değişkeni

    private int spawnTargetTime = 1000;
    Timer spawnTargetTimer = new Timer(spawnTargetTime,new ActionListener() {      
        @Override
        public void actionPerformed(ActionEvent e) {
            int LR = (int)(Math.random()*2); // 0 Left 1 Right
            int x = 10;
            if (LR == 1){
                x = 720;
            }
            int y = 800;
            int size = (int)(Math.random()*3+1);
            targets.add(new Target(x,y,size));
        }
    });
    
    private int spawnCatTimeDefault = 4000;
    private int spawnCatTime = spawnCatTimeDefault;
    Timer spawnCatTimer = new Timer(spawnCatTime,new ActionListener() {      
        @Override
        public void actionPerformed(ActionEvent e) {
            cats.add(new Cat());
        }
    });
    
    
    public Game() {
        try {
            image = ImageIO.read(new FileImageInputStream(new File("Chicken.png")));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            catImg = ImageIO.read(new FileImageInputStream(new File("Cat.png")));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            startImg = ImageIO.read(new FileImageInputStream(new File("start.png")));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            endImg = ImageIO.read(new FileImageInputStream(new File("end.png")));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            tryagainImg = ImageIO.read(new FileImageInputStream(new File("tryagain.png")));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            level2Img = ImageIO.read(new FileImageInputStream(new File("level2.png")));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            level3Img = ImageIO.read(new FileImageInputStream(new File("level3.png")));
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setBackground(Color.BLACK);
        
        
    }
    
    void increaseLevel(){
        eggSpeed -= 3;
        targetSpeed += 3;
        catSpeed += 5;
        spawnCatTime /= 2;
    }
    void setFirstLevel(){
        eggSpeed = eggSpeedDefault;
        targetSpeed = targetSpeedDefault;
        catSpeed = catSpeedDefault;
        spawnCatTime = spawnCatTimeDefault;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    
            if (isPlaying == false){
                //clear();
                switch (levelState){
                    case -1:
                        g.drawImage(tryagainImg,0,0,tryagainImg.getWidth(),tryagainImg.getHeight(),this);
                        break;
                    case 0:
                        g.drawImage(startImg,0,0,startImg.getWidth(),startImg.getHeight(),this);
                        break;
                    case 1:
                        g.drawImage(level2Img,0,0,level2Img.getWidth(),level2Img.getHeight(),this);
                        break;
                    case 2:
                        g.drawImage(level3Img,0,0,level3Img.getWidth(),level3Img.getHeight(),this);
                        break;
                    case 3:
                        g.drawImage(endImg,0,0,endImg.getWidth(),endImg.getHeight(),this);
                        break;
                    default:
                        break;
                }
            }
            else{        
                g.drawImage(image,chickenX,50,image.getWidth()/4,image.getHeight()/4,this);
                g.setColor(Color.BLUE);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 30)); 
                String scoreText = "Score: " + score;
                g.drawString(scoreText, 360, 30);
                
                
                g.setColor(Color.YELLOW);
                for (Egg egg : eggs){
                    int x = egg.getX();
                    if (x < 0 || x > 800){
                        eggs.remove(egg);
                    }
                }
                for (Egg egg : eggs){
                    g.fillOval(egg.getX(), egg.getY(), 30,20);
                }

                g.setColor(Color.RED);
                for (Target target : targets){
                    if (target.getY()<0){
                        targets.remove(target);
                    }
                }
                for (Target target : targets){
                    g.fillOval(target.getX(), target.getY(), target.getSize(), target.getSize());
                }

                for (Cat cat : cats){
                    if (cat.getY() < 0){
                        cats.remove(cat);
                    }
                }
                for (Cat cat : cats){
                    g.drawImage(catImg,cat.getX(),cat.getY(),catImg.getWidth()/25,catImg.getHeight()/25,this);
                }
            }
    }

    @Override
    public void repaint() {
        super.repaint(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();

        if (c == KeyEvent.VK_ENTER && isPlaying == false && levelState != 3){
            if (levelState == -1){
                score = 0;
                levelState = 0;
                setFirstLevel();
            }
            else{
//                clear();
                isPlaying = true;
            }
            timer.start();
            spawnTargetTimer.start();
            spawnCatTimer.start();
        }
        else{        
            switch (c) {
                case KeyEvent.VK_LEFT:
                    if (chickenX >= 120){
                        chickenX -= dirChickenX;
                        repaint();
                    }   break;
                case KeyEvent.VK_RIGHT:
                    if (chickenX <= 580){
                        chickenX += dirChickenX;
                        repaint();
                    }   break;
                case KeyEvent.VK_A:
                    //left fire
                    eggs.add(new Egg(chickenX,50,-1));
                    break;
                case KeyEvent.VK_D:
                    //right fire
                    eggs.add(new Egg(chickenX,50,1));        
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    void shootControl(){
        for (Egg egg : eggs){
            int eggX = egg.getX();
            int eggY = egg.getY();
            if (eggX > 150 && 600 > eggX ){
                continue;
            }

            for (Target target : targets){
                int targetX = target.getX();
                int targetY = target.getY();
                int point = target.getSize();
                if (targetY > 80){
                    continue;
                }
                if (Math.abs(eggY-targetY) < point && Math.abs(eggX-targetX) < point){
                    score += point;
                    
                    targets.remove(target);
                    return;
                }
            }
        }
    }
    
    void clear(){
        eggs.removeAll(eggs);
        targets.removeAll(targets);
        cats.removeAll(cats);
    }
    
    boolean checkCat(){
        if (isPlaying != true){
            return false;
        }
        for (Cat cat : cats){
            int catY = cat.getY();
            if (catY > 80){
                continue;
            }
            int catX = cat.getX();
            if (Math.abs(catX-chickenX) < 80){
                System.out.println("yandin");
                return true;
            }
        }
            return false;
    }
    void checkLevel(){
        if (levelState == 0 && score >= 200){
            levelState++;
            increaseLevel();
            stopGame();
        }
        else if (levelState == 1 && score >= 400){
            levelState++;
            increaseLevel();
            stopGame();
        }
        else if (levelState == 2 && score >= 600){
            levelState++;
            increaseLevel();
            stopGame();
        }
    }
    
    void stopGame(){
        timer.stop();
        spawnCatTimer.stop();
        spawnTargetTimer.stop();
        isPlaying = false;
        
        clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) { //MOVE
        
        for (Egg egg : eggs){
            int temp = egg.getX();
            temp += eggSpeed * egg.getDirection();
            egg.setX(temp);
        }
        for (Target target : targets){
            int temp = target.getY();
            temp -= targetSpeed;
            target.setY(temp);
        }
        for (Cat cat : cats){
            int temp = cat.getY();
            temp -= catSpeed;
            cat.setY(temp);
        }
        if (checkCat()){ //yanma;
            
            stopGame();
            levelState = -1;
        }
        checkLevel();
        shootControl();
        repaint();
    }
    
}
