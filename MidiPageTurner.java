import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import java.lang.Thread;
import java.util.Arrays;
import javax.sound.midi.*;

class MidiPageTurner {
    private static final byte[] NEXT_PAGE_MESSAGE = new byte[] { intToUnsignedByte(ShortMessage.NOTE_ON), 0, 127 };
    private static final byte[] PREV_PAGE_MESSAGE = new byte[] { intToUnsignedByte(ShortMessage.PROGRAM_CHANGE | 0), 0 };

    private static final int NEXT_PAGE_KEYCODE = KeyEvent.VK_PAGE_DOWN;
    private static final int PREV_PAGE_KEYCODE = KeyEvent.VK_PAGE_UP;

    public static void main(String[] args) throws AWTException, InterruptedException, MidiUnavailableException {
        Transmitter midiInPort = MidiSystem.getTransmitter();
        Receiver pageTurner = new PageTurnerReceiver();
        midiInPort.setReceiver(pageTurner);
        Thread.currentThread().join();
    }

    private static byte intToUnsignedByte(int i) {
        return (byte)(i & 0xFF);
    }

    private static class PageTurnerReceiver implements Receiver {

        private final Robot robot;

        public PageTurnerReceiver() throws AWTException {
            robot = new Robot();
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (Arrays.equals(message.getMessage(), NEXT_PAGE_MESSAGE)) {
                System.out.println("Next page!");
                robot.keyPress(NEXT_PAGE_KEYCODE);
                robot.keyRelease(NEXT_PAGE_KEYCODE);
            }
            if (Arrays.equals(message.getMessage(), PREV_PAGE_MESSAGE)) {
                System.out.println("Previous page!");
                robot.keyPress(PREV_PAGE_KEYCODE);
                robot.keyRelease(PREV_PAGE_KEYCODE);
            }
        }

        @Override
        public void close() {
        }
    }
}
