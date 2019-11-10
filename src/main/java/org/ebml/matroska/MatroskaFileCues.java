package org.ebml.matroska;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ebml.Element;
import org.ebml.MasterElement;
import org.ebml.UnsignedIntegerElement;
import org.ebml.io.DataWriter;

public class MatroskaFileCues
{
  private static final Logger LOGGER = Logger.getLogger(MatroskaFileCues.class.getName());
  private MasterElement cues = MatroskaDocTypes.Cues.getInstance();
  private long endOfEbmlHeaderBytePosition;
  
  public MatroskaFileCues(long endOfEbmlHeaderBytePosition)
  {
    this.endOfEbmlHeaderBytePosition = endOfEbmlHeaderBytePosition;
  }

  public void addCue(long positionInFile, long timecodeOfCluster, Collection<Integer> clusterTrackNumbers)
  {
	  LOGGER.log(Level.FINE, "Adding matroska cue to cues element at position [" + positionInFile + "], using timecode ["+ timecodeOfCluster + "], for track numbers [" + clusterTrackNumbers + "]");

    UnsignedIntegerElement cueTime = MatroskaDocTypes.CueTime.getInstance();
    cueTime.setValue(timecodeOfCluster);
    MasterElement cuePoint = MatroskaDocTypes.CuePoint.getInstance();
    MasterElement cueTrackPositions = createCueTrackPositions(positionInFile, clusterTrackNumbers);
    
    cues.addChildElement(cuePoint);
    cuePoint.addChildElement(cueTime);
    cuePoint.addChildElement(cueTrackPositions);
    
    LOGGER.log(Level.FINE, "Finished adding matroska cue to cues element");
  }

  private MasterElement createCueTrackPositions(final long positionInFile, final Collection<Integer> trackNumbers)
  {
    MasterElement cueTrackPositions = MatroskaDocTypes.CueTrackPositions.getInstance();
    
    for (Integer trackNumber : trackNumbers)
    {
      UnsignedIntegerElement cueTrack = MatroskaDocTypes.CueTrack.getInstance();
      cueTrack.setValue(trackNumber);
      
      UnsignedIntegerElement cueClusterPosition = MatroskaDocTypes.CueClusterPosition.getInstance();
      cueClusterPosition.setValue(getPositionRelativeToSegmentEbmlElement(positionInFile));
      
      cueTrackPositions.addChildElement(cueTrack);
      cueTrackPositions.addChildElement(cueClusterPosition);
    }
    return cueTrackPositions;
  }
  
  public Element write(DataWriter ioDW, MatroskaFileMetaSeek metaSeek)
  {
    long currentBytePositionInFile = ioDW.getFilePointer();
    LOGGER.log(Level.FINE, "Writing matroska cues at file byte position [{0}]", currentBytePositionInFile);
    long numberOfBytesInCueData = cues.writeElement(ioDW);
    LOGGER.log(Level.FINE, "Done writing matroska cues, number of bytes was [{0}]", numberOfBytesInCueData);
    
    metaSeek.addIndexedElement(cues, getPositionRelativeToSegmentEbmlElement(currentBytePositionInFile));
    
    return cues;
  }

  private long getPositionRelativeToSegmentEbmlElement(long currentBytePositionInFile)
  {
    return currentBytePositionInFile - endOfEbmlHeaderBytePosition;
  }
}
