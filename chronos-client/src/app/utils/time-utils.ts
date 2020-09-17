export function calculateTimeInTimeZone(differenceFromGmt: string) {
  const now = new Date();
  const utcNowMillis = now.getTime() + now.getTimezoneOffset() * 60000;

  const isPositive = differenceFromGmt.charAt(0) === '+';
  const numericalDifference = differenceFromGmt.substring(1);
  const [hours, minutes] = numericalDifference.split(':');

  let offset = Number(hours) + Number(minutes) / 60;
  offset = isPositive ? offset : offset * -1;
  const nowInTimeZone = new Date(utcNowMillis + 3600000 * offset);
  return nowInTimeZone;
}

export function convertToTwoDigitFormat(value: number) {
  if (value < 10) {
    return `0${value}`;
  }
  return String(value);
}
