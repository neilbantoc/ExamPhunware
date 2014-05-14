package com.stratpoint.phunware.homework.common.model;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
 
public class ScheduleItem {
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
	private static final SimpleDateFormat DAY_AND_TIME_FORMATTER = new SimpleDateFormat("ccccc MM/dd HH:mm aa", Locale.US);
	private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm aa", Locale.US);
 
	@JsonProperty("start_date")
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private Date mStartDate;
	@JsonProperty("end_date")
	@JsonDeserialize(using = DateDeserializer.class)
	@JsonSerialize(using = DateSerializer.class)
	private Date mEndDate;
 
	public ScheduleItem() {
 
	}
 
	public ScheduleItem(Date startDate, Date endDate) {
		mStartDate = startDate;
		mEndDate = endDate;
	}
 
	public ScheduleItem(String startDate, String endDate) throws ParseException {
		mStartDate = FORMATTER.parse(startDate);
		mEndDate = FORMATTER.parse(endDate);
	}
 
	public Date getStartDate() {
		return mStartDate;
	}
 
	public void setStartDate(Date startDate) {
		mStartDate = startDate;
	}
 
	public Date getEndDate() {
		return mEndDate;
	}
 
	public void setEndDate(Date endDate) {
		mEndDate = endDate;
	}
 
	@JsonSetter
	public void setStartDate(String startDate) throws ParseException {
		mStartDate = FORMATTER.parse(startDate);
	}
 
	@JsonSetter
	public void setEndDate(String endDate) throws ParseException {
		mEndDate = FORMATTER.parse(endDate);
	}
 
	public String getStartDateString() {
		return FORMATTER.format(mStartDate);
	}
 
	public String getEndDateString() {
		return FORMATTER.format(mEndDate);
	}
 
	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if (o instanceof ScheduleItem) {
			result = mStartDate.equals(((ScheduleItem) o).getStartDate())
					&& mEndDate.equals(((ScheduleItem) o).getEndDate());
		}
		return result;
	}
 
	@Override
	public int hashCode() {
		String s = getStartDateString() + getEndDateString();
		return s.hashCode();
	}
	
	public String getStartToEndDateFormattedString(){
		return DAY_AND_TIME_FORMATTER.format(mStartDate) + " to " + TIME_FORMATTER.format(mEndDate);
	}
 
	@JsonIgnoreType
	public static class DateSerializer extends JsonSerializer<Date> {
	    @Override
	    public void serialize(Date mDate, JsonGenerator mJsonGenerator, SerializerProvider mSerializerProvider) throws IOException, JsonProcessingException {
	        mJsonGenerator.writeString(FORMATTER.format(mDate));
	    }
	}
	
	@JsonIgnoreType
	public static class DateDeserializer extends JsonDeserializer<Date> {
	    @Override
	    public Date deserialize(JsonParser mJsonParser, DeserializationContext mDeserializationcontext) throws IOException, JsonProcessingException {
            try {
				return FORMATTER.parse(mJsonParser.getText());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
	    }
	}
}