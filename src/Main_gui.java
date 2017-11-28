import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.ws.Service;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import com.ibm.watson.developer_cloud.visual_recognition.v3.*;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;


public class Main_gui {
  String path; 
  public void setPath(String path) 
  {
	  this.path = path;
  }
  public String getPath() 
  {
	  return path;
  }
  

  		
  private String[] pathSplit;
  private String imagePath;
  private String imageName;
  private VisualRecognition service;
  private String defaultPath = "/Users/peijiexu/Downloads/savedImg/";
  private String savePath;
	
	
  Display d;
  Shell s;
  
  Main_gui() {
	  
	  service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
	  service.setApiKey("8784a9abd0f36a71099e00b25b0a156f2b79cfc5");
	  //basic construct initialize the gui
    d = new Display();
    s = new Shell(d,SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX);
    //auto quit the gui when red x pressed
    s.addListener(SWT.CLOSE, new Listener() 
    {
    	   public void handleEvent(Event e)
    	   {
    		   s.setVisible(false);
    	   }
    	});
   
    GridLayout gl_s = new GridLayout(3, true);
    gl_s.horizontalSpacing = 20;
    gl_s.verticalSpacing = 10;
    gl_s.marginHeight = 5;
    Label tree_label = new Label(s, SWT.NONE);
    Label pic_label = new Label(s, SWT.NONE);
    Label watson_label = new Label(s, SWT.NONE);
    Tree tree = new Tree(s, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
    Label imageField = new Label(s, SWT.BORDER | SWT.SCROLL_LINE);
    Text watsonOutput = new Text(s, SWT.BORDER|SWT.MULTI);
    Text txtOutput = new Text(s, SWT.BORDER);
    Button btnAnalyze = new Button(s, SWT.NONE);
    Button btnReset = new Button(s, SWT.NONE);
    

    
    GridData gd_tl = new GridData();
    GridData gd_pl = new GridData();
    GridData gd_wl = new GridData();
    GridData gd_tree = new GridData();
    GridData gd_image = new GridData();
    GridData gd_watson = new GridData();
    GridData gd_txt = new GridData();
    GridData gd_analyze = new GridData();
    GridData gd_reset = new GridData();
    
    gd_tl.horizontalAlignment = GridData.CENTER;
    gd_tl.verticalAlignment = GridData.END;
    gd_tl.grabExcessHorizontalSpace = true;
    gd_tl.grabExcessVerticalSpace = false;
    gd_pl.horizontalAlignment = GridData.CENTER;
    gd_pl.verticalAlignment = GridData.END;
    gd_pl.grabExcessHorizontalSpace = true;
    gd_pl.grabExcessVerticalSpace = false;
    gd_wl.horizontalAlignment = GridData.CENTER;
    gd_wl.verticalAlignment = GridData.END;
    gd_wl.grabExcessHorizontalSpace = true;
    gd_wl.grabExcessVerticalSpace = false;
    gd_tree.horizontalAlignment = GridData.FILL;
    gd_tree.grabExcessHorizontalSpace = true;
    gd_tree.grabExcessVerticalSpace = true;
    gd_tree.heightHint = 800;
    gd_image.horizontalAlignment = GridData.FILL;
    gd_image.grabExcessHorizontalSpace = true;
    gd_image.grabExcessVerticalSpace = true;
    gd_image.heightHint = 800;
    gd_watson.horizontalAlignment = GridData.FILL;
    gd_watson.grabExcessHorizontalSpace = true;
    gd_watson.grabExcessVerticalSpace = true;
    gd_watson.heightHint = 800;
    gd_txt.horizontalAlignment = GridData.FILL;
    gd_txt.grabExcessHorizontalSpace = true;
    gd_txt.grabExcessVerticalSpace = true;
    gd_analyze.horizontalAlignment = GridData.FILL;
    gd_analyze.grabExcessHorizontalSpace = true;
    gd_analyze.grabExcessVerticalSpace = false;
    gd_reset.horizontalAlignment = GridData.FILL;
    gd_reset.grabExcessHorizontalSpace = true;
    gd_reset.grabExcessVerticalSpace = false;
    
    tree_label.setLayoutData(gd_tl);
    pic_label.setLayoutData(gd_pl);
    watson_label.setLayoutData(gd_wl);
    tree.setLayoutData(gd_tree);
    imageField.setLayoutData(gd_image);
    watsonOutput.setLayoutData(gd_watson);
    txtOutput.setLayoutData(gd_txt);
    btnAnalyze.setLayoutData(gd_analyze);
    btnReset.setLayoutData(gd_reset);
    s.setLayout(gl_s);
    
    
    
    btnAnalyze.setText("Analyze Image");
    btnReset.setText("Reset");
    watsonOutput.setEditable(false);
    txtOutput.setEditable(false);
    tree_label.setText("File Explorer");
    pic_label.setText("Image Preview");
    watson_label.setText("Watson Analysis");
    
    //BELOW ALTERS IMAGES AND MAKES THEM INVALID.  FIX
    	
   btnAnalyze.addSelectionListener(new SelectionAdapter() {
	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			InputStream imagesStream = new FileInputStream(imagePath);
			System.out.println(imagePath);
			ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
					  .imagesFile(imagesStream)
					  .imagesFilename(imageName)
					  .parameters("{\"classifier_ids\": [\"astronomy_1713785246\"]}")
					  .build();
					ClassifiedImages result = service.classify(classifyOptions).execute();
					watsonOutput.setText(result.toString());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			txtOutput.setText(imageName);
		}
		catch(Exception ex) {
			txtOutput.setText("No Image Selected");
		}
	}
});


    

    btnReset.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		watsonOutput.setText("");
    		txtOutput.setText("");
    		imageField.setImage(null);
    		imagePath = null;
    		imageName = null;
    		savePath = null;
    	}
    });


    s.setLayout(gl_s);
    s.open();
    
    s.setText("CTN_GUI");
    //         create the menu system
    Menu m = new Menu(s, SWT.BAR);
    // create a file menu and add an exit item
    final MenuItem file = new MenuItem(m, SWT.CASCADE);
    file.setText("&File");
    final Menu filemenu = new Menu(s, SWT.DROP_DOWN);
    file.setMenu(filemenu);
    final MenuItem openItem = new MenuItem(filemenu, SWT.PUSH);
    openItem.setText("&Open\tCTRL+O");
    openItem.setAccelerator(SWT.CTRL + 'O');
    final MenuItem saveItem = new MenuItem(filemenu, SWT.PUSH);
    saveItem.setText("&Save\tCTRL+S");
    saveItem.setAccelerator(SWT.CTRL + 'S');
    final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
    final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
    exitItem.setText("E&xit");



    class Open implements SelectionListener {
      public void widgetSelected(SelectionEvent event) {
        FileDialog fd = new FileDialog(s, SWT.OPEN);
        fd.setText("Open");
        fd.setFilterPath("C:/");
        String[] filterExt = {  "*.*","*.txt", "*.doc", ".rtf"};
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        setPath(selected);
        System.out.println(selected);
        try 
        {
        	// using "/" to split string in mac
        	
        	pathSplit = selected.split("/");
        	imageName = pathSplit[pathSplit.length-1];
        	savePath = defaultPath + imageName;
        	
        	     Image image = SWTResourceManager.getImage(selected);
             ImageData imgData = image.getImageData();
             imgData = imgData.scaledTo(imageField.getBounds().width, imageField.getBounds().height);
             ImageLoader imgLoader = new ImageLoader();
             imgLoader.data = new ImageData[] {imgData};
             imgLoader.save(savePath, SWT.IMAGE_COPY);
             imageField.setBounds(imageField.getBounds().x,imageField.getBounds().y,imageField.getBounds().width,
             						imageField.getBounds().height);
             imageField.setImage(SWTResourceManager.getImage(savePath));
        	
        	imagePath = selected;
        	System.out.println(imageName);
        	System.out.println(savePath);
        	System.out.println(imagePath);

           // imageField.setImage(SWTResourceManager.getImage(selected));
            
            
            
           	}
        catch(Exception e) 
        {
        	 System.out.println(e);
        }
        
      }

      public void widgetDefaultSelected(SelectionEvent event) {
    	      System.out.println("hello");
      }
    }

    class Save implements SelectionListener {
      public void widgetSelected(SelectionEvent event) {
        FileDialog fd = new FileDialog(s, SWT.SAVE);
        fd.setText("Save");
        fd.setFilterPath("C:/");
        String[] filterExt = { "*.*", "*.txt", "*.doc", ".rtf" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        
        System.out.println(selected);
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    }
    openItem.addSelectionListener(new Open());
    saveItem.addSelectionListener(new Save());

    exitItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        MessageBox messageBox = new MessageBox(s, SWT.ICON_QUESTION
            | SWT.YES | SWT.NO);
        messageBox.setMessage("Do you really want to exit?");
        messageBox.setText("Exiting Application");
        int response = messageBox.open();
        if (response == SWT.YES)
          System.exit(0);
      }
    });
    s.setMenuBar(m);
    
 
    


    while (!s.isDisposed()) {
      if (!d.readAndDispatch())
        d.sleep();
    }
    d.dispose();
  }
  

  public static void main(String[] argv) {
    new Main_gui();
  }
}
