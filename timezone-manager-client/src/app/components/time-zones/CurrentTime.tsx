import React from 'react';
import { useClock } from '../../hooks/useClock';
import { convertToTwoDigitFormat } from '../../utils/time-utils';

interface Props {
  differenceFromGmt: string;
}

const CurrentTime: React.FC<Props> = props => {
  const { differenceFromGmt } = props;
  const time = useClock(differenceFromGmt);
  const day = time.toDateString();
  const hours = convertToTwoDigitFormat(time.getHours());
  const minutes = convertToTwoDigitFormat(time.getMinutes());
  const seconds = convertToTwoDigitFormat(time.getSeconds());
  const currentTimeInTimeZone = `${day}, ${hours}:${minutes}:${seconds} ${differenceFromGmt} GMT`;

  return <>{currentTimeInTimeZone}</>;
};

export default CurrentTime;
