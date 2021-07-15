public class GuitarHero {

    private static synthesizer.GuitarString[] strings = new synthesizer.GuitarString[37];
    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {

        for (int i = 0; i < strings.length; i++) {
            double freq = 440.0 * Math.pow(2, (i - 24.) / 12.);
            strings[i] = new synthesizer.GuitarString(freq);
        }

        while (true) {

//            int index = -1;
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index >= 0) {
                    strings[index].pluck();
                }
            }
            /* compute the superposition of samples */
            double sample = 0;
            for (synthesizer.GuitarString gs : strings) {
                sample += gs.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (synthesizer.GuitarString gs : strings) {
                gs.tic();
            }
        }
    }
}
