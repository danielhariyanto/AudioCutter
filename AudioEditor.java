import java.io.*;
import java.util.Scanner;
import javax.sound.sampled.*;

public class AudioEditor {
	
	
    public static void main(String[] args) throws FileNotFoundException{
    	
    	File myObj = new File("tele01a.cha");
		Scanner myReader = new Scanner(myObj);
		while (myReader.hasNext()) {
			int i = 0;
			int j = 0;
			String word = myReader.next();
			if (word.equals("*INV:")) {
				while (myReader.hasNext()) {
					String time = myReader.next();
					if (time.contains("_") && !time.matches(".*[a-zA-Z]+.*")) {
						String times[] = time.split("_");
						float startSecond = Float.parseFloat(times[0]);
						float endSecond = Float.parseFloat(times[1]);
						cutAudio("tele01a.wav",String.format("control%d.wav",i),startSecond,endSecond);
						i++;
						break;
					}
				}
			} else if (word.equals("*PAR:")) {
				while (myReader.hasNext()) {
					String time = myReader.next();
					if (time.contains("_") && !time.matches(".*[a-zA-Z]+.*")) {
						String times[] = time.split("_");
						float startSecond = Float.parseFloat(times[0]);
						float endSecond = Float.parseFloat(times[1]);
						cutAudio("tele01a.wav",String.format("dementia%d.wav",j),startSecond,endSecond);
						j++;
						break;
					}
				}
			}
				
	    }
		myReader.close();
    }
    
    public static void convertWAV(String wavFile) {
    	
    }
    
    public static void appendAudio(String wavFile1, String wavFile2) {
        try {
	      AudioInputStream clip1 = AudioSystem.getAudioInputStream(new File(wavFile1));
	      AudioInputStream clip2 = AudioSystem.getAudioInputStream(new File(wavFile2));
	
	      AudioInputStream appendedFiles =
	    		  new AudioInputStream(
	    				  new SequenceInputStream(clip1, clip2),
	    				  clip1.getFormat(),
	    				  clip1.getFrameLength() + clip2.getFrameLength());
	
	        AudioSystem.write(appendedFiles,
	        		AudioFileFormat.Type.WAVE,
	        		new File("wavAppended.wav"));
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static void cutAudio(String sourceFileName, String destinationFileName, float startSecond, float endSecond) {
		AudioInputStream inputStream = null;
		AudioInputStream shortenedStream = null;
		startSecond = startSecond/1000;
		endSecond = endSecond/1000;
		float secondsToCopy = endSecond - startSecond;
		try {
			File file = new File(sourceFileName);
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			AudioFormat format = fileFormat.getFormat();
			inputStream = AudioSystem.getAudioInputStream(file);
			int bytesPerSecond = format.getFrameSize() * (int)format.getFrameRate();
			inputStream.skip((int)(startSecond * bytesPerSecond));
			long framesOfAudioToCopy = (int)(secondsToCopy * (int)format.getFrameRate());
			shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
			File destinationFile = new File(destinationFileName);
			AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (inputStream != null) try { inputStream.close(); } catch (Exception e) { System.out.println(e); }
			if (shortenedStream != null) try { shortenedStream.close(); } catch (Exception e) { System.out.println(e); }
			}
		}
}
