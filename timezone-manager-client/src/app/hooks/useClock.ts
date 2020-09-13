import { useEffect, useState } from 'react';
import { calculateTimeInTimeZone } from '../utils/time-utils';

export const useClock = (differenceFromGmt: string) => {
  const initialTime = calculateTimeInTimeZone(differenceFromGmt);
  const [time, setTime] = useState(initialTime);

  useEffect(() => {
    const id = setInterval(() => {
      setTime(() => calculateTimeInTimeZone(differenceFromGmt));
    }, 1000);
    return () => clearInterval(id);
  }, [differenceFromGmt]);

  return time;
};
