import java.util.Date;

import junit.framework.TestCase;
import kr.ac.hyu.kangdaecheol.calendar.model.BoardInfo;
import kr.ac.hyu.kangdaecheol.calendar.model.MyDate;
import kr.ac.hyu.kangdaecheol.calendar.model.Schedule;

public class DAECHEOL extends TestCase {
	public DAECHEOL(String name) {
		super(name);
		testSchedule();
		testSchedule100000case();
		testDate();
		testDate100case();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSchedule() {
		Schedule sc = new Schedule();
		Date start_date = new Date();
		Date end_date = new Date();

		sc.setContents("Hi");
		sc.setStartDate(start_date);
		sc.setEndDate(end_date);

		assertEquals(sc.getContents(), "Hi");
		assertEquals(sc.getStartDate(), start_date);
		assertEquals(sc.getEndDate(), end_date);

	}

	public void testSchedule100000case() {
		for (int i = 0; i < 100000; i++) {
			Schedule sc = new Schedule();
			Date start_date = new Date();
			Date end_date = new Date();

			sc.setContents("Hi" + i);
			sc.setStartDate(start_date);
			sc.setEndDate(end_date);

			assertEquals(sc.getContents(), "Hi" + i);
			assertEquals(sc.getStartDate(), start_date);
			assertEquals(sc.getEndDate(), end_date);
		}
	}

	public void testDate() {
		MyDate d = new MyDate();
		d.setDay(1);
		d.setMonth(4);
		d.setYear(2013);

		assertEquals(d.getDay(), 1);
		assertEquals(d.getMonth(), 4);
		assertEquals(d.getYear(), 2013);

		MyDate d2 = new MyDate();
		d2.setDay(12);
		d2.setMonth(6);
		d2.setYear(2033);

		assertEquals(d2.getDay(), 12);
		assertEquals(d2.getMonth(), 6);
		assertEquals(d2.getYear(), 2033);
	}

	public void testDate100case() {
		for (int i = 0; i < 100; i++) {
			MyDate d = new MyDate();
			d.setDay(1);
			d.setMonth(4);
			d.setYear(2013 + i);

			assertEquals(d.getDay(), 1);
			assertEquals(d.getMonth(), 4);
			assertEquals(d.getYear(), 2013 + i);
		}
	}

	public void testBoardInfo() {
		BoardInfo bi = new BoardInfo();
		bi.setId(1);
		bi.setAuthor("DAECHEOL, KANG");
		bi.setContent("Hi");
		bi.setDay("10");
		bi.setMonth("6");
		bi.setYear("2014");

		assertEquals(bi.getId(), 1);
		assertEquals(bi.getAuthor(), "DAECHEOL, KANG");
		assertEquals(bi.getContent(), "Hi");
		assertEquals(bi.getDay(), "10");
		assertEquals(bi.getMonth(), "6");
		assertEquals(bi.getYear(), "2014");
	}

}
