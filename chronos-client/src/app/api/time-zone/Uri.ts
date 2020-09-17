export const TIME_ZONES_URI = '/api/v1/time-zones';

export const TIME_ZONE_URI = (uid: string) => `${TIME_ZONES_URI}/${uid}`;
