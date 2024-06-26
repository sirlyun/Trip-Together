import {TRIP_API_URL} from '@env';
import {AxiosError, AxiosResponse, RawAxiosRequestConfig} from 'axios';
import getToken from '../../hooks/getToken';
import {PaginationResponse} from '../../interfaces/hooks/PaginationResponse';
import {
  CardHistoryState,
  cardHistory,
} from '../../interfaces/states/CardHistoryState';
import {useAppDispatch} from '../../store/hooks';
import {pushContent} from '../../store/slices/cardHistory';
import useAxois from '../useAxois';

interface GetCardHistoryParams {
  page: number;
  size: number;
  sort: 'DESC' | 'ASC';
  currency_code?: string;
}

interface GetCardHistoryResponse extends PaginationResponse<cardHistory> {
  status: number;
  message: string;
}

const useGetCardHistory = () => {
  const axios = useAxois();
  const dispatch = useAppDispatch();

  const getCardHistoryConfig = async (params: GetCardHistoryParams) => {
    const {access_token} = await getToken();

    const axiosConfig: RawAxiosRequestConfig = {
      url: `${TRIP_API_URL}/api/account/v1/trip-account/account-histories`,
      method: 'get',
      headers: {
        Authorization: `Bearer ${access_token}`,
      },
      params,
    };

    return axiosConfig;
  };

  const getCardHistory = async (params: GetCardHistoryParams) => {
    const result = await axios
      .request(await getCardHistoryConfig(params))
      .then((res: AxiosResponse<GetCardHistoryResponse>) => {
        const payload: CardHistoryState = {
          content: res.data.data.content,
          pageable: {
            pageNumber: res.data.data.pageable.pageNumber,
            pageSize: res.data.data.pageable.pageSize,
            last: res.data.data.last,
            first: res.data.data.first,
          },
        };
        dispatch(pushContent(payload));
      })
      .catch((err: AxiosError) => {
        console.error(err);
      });

    return result;
  };

  return getCardHistory;
};

export type {GetCardHistoryParams};
export default useGetCardHistory;
