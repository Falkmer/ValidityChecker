import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final Logger logger = LogManager.getLogger(Validator.class);
    private abstract class AbstractValidator {
        private boolean isValid;
        private String subject;

        public AbstractValidator(String subject) {
            this.isValid = true;
            this.subject = subject;
        }

        public void checkControlDigit(String input) {
            if (input != null){
                int sum = 0, tmp, ctrl;
                ctrl = Integer.parseInt(input.substring(input.length()-1));

                for (int i = 0; i<input.length()-1; i++){
                    tmp = Integer.parseInt(input.substring(i, i+1));

                    if (i%2==0){
                        tmp = tmp*2;
                    }

                    if (tmp > 9){
                        sum += 1+(tmp%10);
                    } else {
                        sum += tmp;
                    }
                }

                sum = (10-(sum%10))%10;

                if (sum != ctrl){
                    setValid(false);
                    logger.error("AbstractValidator checkControlDigit: Invalid control digit");
                }
            } else {
                setValid(false);
                logger.error("AbstractValidator checkControlDigit: Invalid input to method");
            }
        }

        public void checkDate(String dob){
            try {
                int year, month, day;

                year = Integer.parseInt(dob.substring(0,4));
                month = Integer.parseInt(dob.substring(4,6));
                day = Integer.parseInt(dob.substring(6,8));

                LocalDate date = LocalDate.of(year, month, day);
                LocalDate now = LocalDate.now();
                if (date.isAfter(now)){
                    logger.error("AbstractValidator checkDate: Date has yet to pass and don't belong to an existing person");
                    setValid(false);
                }
            } catch (Exception e) {
                logger.error("AbstractValidator checkDate: Date is invalid");
                setValid(false);
            }
        }

        public boolean isValid() {
            return isValid;
        }

        public void setValid(boolean state) {
            this.isValid = state;
        }

        public String getSubject() {
            return subject;
        }
    }

    /**
     *  Bellow are validator classes for Swedish social security numbers
     **/
    private abstract class SSNValidator extends AbstractValidator {
        public SSNValidator(String subject) {
            super(subject);
        }

        public abstract void checkDate();
        public abstract void checkControlDigit();

    }

    private class ShortDivSSNValidator extends SSNValidator {
        public ShortDivSSNValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = "00" + this.getSubject().substring(0, 6);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(0, 6) + compSubject.substring(7, 11);
            super.checkControlDigit(compSubject);
        }
    }

    private class ShortSSNValidator extends SSNValidator {

        public ShortSSNValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = "00" + this.getSubject().substring(0, 6);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            super.checkControlDigit(this.getSubject());
        }

    }

    private class LongSSNValidator extends SSNValidator {
        public LongSSNValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = this.getSubject().substring(0, 8);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(2, 12);;
            super.checkControlDigit(compSubject);
        }
    }

    private class LongDivSSNValidator extends SSNValidator {
        public LongDivSSNValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = this.getSubject().substring(0, 8);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(2, 8)+compSubject.substring(9, 13);
            super.checkControlDigit(compSubject);
        }
    }

    /**
     *  Bellow are validator classes for Organisation numbers
     **/
    private abstract class OrgNumValidator extends AbstractValidator {
        public OrgNumValidator(String subject) {
            super(subject);
        }

        public abstract void checkControlDigit();
        public abstract void checkOrgNumSemantics();
    }

    private class ShortDivOrgNumValidator extends OrgNumValidator {
        public ShortDivOrgNumValidator(String subject) {
            super(subject);
        }

        public void checkOrgNumSemantics(){
            if (Integer.parseInt(this.getSubject().substring(2,3)) < 2){
                this.setValid(false);
                logger.error("ShortDivOrgNumValidator checkOrgNumSemantics: Middle number pare is less than 20");
            }
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(0, 6) + compSubject.substring(7, 11);
            super.checkControlDigit(compSubject);
        }
    }

    private class ShortOrgNumValidator extends OrgNumValidator {
        public ShortOrgNumValidator(String subject) {
            super(subject);
        }

        public void checkOrgNumSemantics(){
            if (Integer.parseInt(this.getSubject().substring(2,3)) < 2){
                this.setValid(false);
                logger.error("ShortOrgNumValidator checkOrgNumSemantics: Middle number pare is less than 20");
            }
        }

        public void checkControlDigit() {
            super.checkControlDigit(this.getSubject());
        }

    }

    private class LongOrgNumValidator extends OrgNumValidator {
        public LongOrgNumValidator(String subject) {
            super(subject);
        }

        public void checkOrgNumSemantics(){
            if (Integer.parseInt(this.getSubject().substring(0,2)) != 16){
                this.setValid(false);
                logger.error("LongOrgNumValidator checkOrgNumSemantics: Start number pare is not 16");
            }

            if (Integer.parseInt(this.getSubject().substring(4,5)) < 2){
                this.setValid(false);
                logger.error("LongOrgNumValidator checkOrgNumSemantics: Middle number pare is less than 20");
            }
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(2, 12);;
            super.checkControlDigit(compSubject);
        }
    }

    private class LongDivOrgNumValidator extends OrgNumValidator {
        public LongDivOrgNumValidator(String subject) {
            super(subject);
        }

        public void checkOrgNumSemantics(){
            if (Integer.parseInt(this.getSubject().substring(0,2)) != 16){
                this.setValid(false);
                logger.error("LongDivOrgNumValidator checkOrgNumSemantics: Start number pare is not 16");
            }

            if (Integer.parseInt(this.getSubject().substring(4,5)) < 2){
                this.setValid(false);
                logger.error("LongDivOrgNumValidator checkOrgNumSemantics: Middle number pare is less than 20");
            }
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(2, 8)+compSubject.substring(9, 13);
            super.checkControlDigit(compSubject);
        }
    }

    /**
     *  Bellow are validator classes for Coordination numbers
     **/
    private abstract class CoordNumValidator extends AbstractValidator {
        public CoordNumValidator(String subject) {
            super(subject);
        }

        public abstract void checkDate();
        public abstract void checkControlDigit();
    }

    private class ShortDivCoordNumValidator extends CoordNumValidator {
        public ShortDivCoordNumValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = "00"+this.getSubject().substring(0, 4);
            int i = Integer.parseInt(this.getSubject().substring(4, 6)) - 60;
            dob += String.valueOf(i);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(0, 6) + compSubject.substring(7, 11);
            super.checkControlDigit(compSubject);
        }
    }

    private class ShortCoordNumValidator extends CoordNumValidator {
        public ShortCoordNumValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = "00"+this.getSubject().substring(0, 4);
            int i = Integer.parseInt(this.getSubject().substring(4, 6)) - 60;
            dob += String.valueOf(i);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            super.checkControlDigit(this.getSubject());
        }

    }

    private class LongCoordNumValidator extends CoordNumValidator {
        public LongCoordNumValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = this.getSubject().substring(0, 6);
            int i = Integer.parseInt(this.getSubject().substring(6, 8)) - 60;
            dob += String.valueOf(i);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(2, 12);;
            super.checkControlDigit(compSubject);
        }
    }

    private class LongDivCoordNumValidator extends CoordNumValidator {
        public LongDivCoordNumValidator(String subject) {
            super(subject);
        }

        public void checkDate() {
            String dob = this.getSubject().substring(0, 6);
            int i = Integer.parseInt(this.getSubject().substring(6, 8)) - 60;
            dob += String.valueOf(i);
            super.checkDate(dob);
        }

        public void checkControlDigit() {
            String compSubject = this.getSubject();
            compSubject = compSubject.substring(2, 8)+compSubject.substring(9, 13);
            super.checkControlDigit(compSubject);
        }
    }

    /**
     *  Bellow are validator methods and valid basic input patterns
     **/
    private final Pattern shortNoDividerPattern = Pattern.compile("^\\d{10}");
    private final Pattern shortWithDividerPattern = Pattern.compile("^\\d{6}([+]|-)\\d{4}");
    private final Pattern longNoDividerPattern = Pattern.compile("^\\d{12}");
    private final Pattern longWithDividerPattern = Pattern.compile("^\\d{8}-\\d{4}");
    private final Pattern shortOrgNumWithDividerPattern = Pattern.compile("^\\d{6}-\\d{4}");
    private final Pattern longOrgNumWithDividerPattern = Pattern.compile("^\\d{8}-\\d{4}");

    public boolean isSSNValid(String input){
        SSNValidator validator = getSSNValidator(input);
        if (validator != null){
            validator.checkDate();
            validator.checkControlDigit();
            return validator.isValid();
        } else {
            logger.error("isSSNValid: Input did not match any of the basic formats");
            return false;
        }
    }

    private SSNValidator getSSNValidator(String subject){
        Matcher shortNoDividerMatcher = shortNoDividerPattern.matcher(subject);
        Matcher shortWithDividerMatcher = shortWithDividerPattern.matcher(subject);
        Matcher longNoDividerMatcher = longNoDividerPattern.matcher(subject);
        Matcher longWithDividerMatcher = longWithDividerPattern.matcher(subject);

        if (shortNoDividerMatcher.matches()) {
            return new ShortSSNValidator(subject);
        } else if (shortWithDividerMatcher.matches()) {
            return new ShortDivSSNValidator(subject);
        } else if (longNoDividerMatcher.matches()) {
            return new LongSSNValidator(subject);
        } else if (longWithDividerMatcher.matches()) {
            return new LongDivSSNValidator(subject);
        } else {
            return null;
        }
    }


    public boolean isCoordNumValid(String input){
        CoordNumValidator validator = getCoordNumValidator(input);
        if (validator != null){
            validator.checkDate();
            validator.checkControlDigit();
            return validator.isValid();
        } else {
            logger.error("isSSNValid: Input did not match any of the basic formats");
            return false;
        }
    }

    private CoordNumValidator getCoordNumValidator(String subject){
        Matcher shortNoDividerMatcher = shortNoDividerPattern.matcher(subject);
        Matcher shortWithDividerMatcher = shortWithDividerPattern.matcher(subject);
        Matcher longNoDividerMatcher = longNoDividerPattern.matcher(subject);
        Matcher longWithDividerMatcher = longWithDividerPattern.matcher(subject);

        if (shortNoDividerMatcher.matches()) {
            return new ShortCoordNumValidator(subject);
        } else if (shortWithDividerMatcher.matches()) {
            return new ShortDivCoordNumValidator(subject);
        } else if (longNoDividerMatcher.matches()) {
            return new LongCoordNumValidator(subject);
        } else if (longWithDividerMatcher.matches()) {
            return new LongDivCoordNumValidator(subject);
        } else {
            return null;
        }
    }

    public boolean isOrgNumValid(String input){
        OrgNumValidator validator = getOrgNumValidator(input);
        if (validator != null){
            validator.checkOrgNumSemantics();
            validator.checkControlDigit();
            return validator.isValid();
        } else {
            logger.error("isSSNValid: Input did not match any of the basic formats");
            return false;
        }
    }

    private OrgNumValidator getOrgNumValidator(String subject){
        Matcher shortNoDividerMatcher = shortNoDividerPattern.matcher(subject);
        Matcher shortOrgNumWithDividerMatcher = shortOrgNumWithDividerPattern.matcher(subject);
        Matcher longNoDividerMatcher = longNoDividerPattern.matcher(subject);
        Matcher longOrgNumWithDividerMatcher = longOrgNumWithDividerPattern.matcher(subject);

        if (shortNoDividerMatcher.matches()) {
            return new ShortOrgNumValidator(subject);
        } else if (shortOrgNumWithDividerMatcher.matches()) {
            return new ShortDivOrgNumValidator(subject);
        } else if (longNoDividerMatcher.matches()) {
            return new LongOrgNumValidator(subject);
        } else if (longOrgNumWithDividerMatcher.matches()) {
            return new LongDivOrgNumValidator(subject);
        } else {
            return null;
        }
    }
}
