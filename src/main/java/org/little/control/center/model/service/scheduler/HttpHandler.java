package org.little.control.center.model.service.scheduler;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.little.control.center.model.MyErrorCode;
import org.little.control.center.model.constant.SchedulerConsts;
import org.little.control.center.model.entity.scheduler.JobInfo;
import org.little.control.center.model.entity.scheduler.param.HttpHandlerParam;
import net.xctec.framework.core.util.MyJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;

/**
 * HTTP请求。
 */
@Service
public class HttpHandler extends BaseHandler implements Handler<HttpHandlerParam> {

    @Autowired
    @Qualifier("httpClient")
    private CloseableHttpClient httpclient;

    @Autowired
    @Qualifier("httpAsyncClient")
    private CloseableHttpAsyncClient httpAsyncClient;

    public HttpHandler() {
        HandlerManager.register(SchedulerConsts.Handler.HTTP, this);
    }

    /**
     * 处理任务。
     * @param jobInfo 任务信息
     * @param handlerParam 执行器参数。
     */
    @Override
    public void process(JobInfo jobInfo, HttpHandlerParam handlerParam) {
        if (StringUtils.isBlank(handlerParam.getUrl())) {
            logger.error("job [{}], url is blank", MyJsonHelper.toJson(jobInfo));
            return;
        }
        if (handlerParam.getAsync() == null || !handlerParam.getAsync()) {
            syncRequest(jobInfo, handlerParam.getMethod(), handlerParam.getUrl());
        } else {
            asyncRequest(jobInfo, handlerParam.getMethod(), handlerParam.getUrl());
        }

    }//end process

    /**
     * 异步请求。
     * @param jobInfo
     * @param method
     * @param url
     */
    private void asyncRequest(final JobInfo jobInfo, final String method, final String url) {
        final String message = "async request error: url = " + url + ".";
        try {
            HttpUriRequest request = null;
            if (StringUtils.isBlank(method) || method.equalsIgnoreCase("get")) {
                request = new HttpGet(url);
            } else {
                request = new HttpPost(url);
            }

            httpAsyncClient.execute(request, new FutureCallback<HttpResponse>() {

                @Override
                public void completed(final HttpResponse response) {

                    int statusCode = response.getStatusLine().getStatusCode();
                    try {
                        if (statusCode == HttpStatus.SC_OK) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("async request success: url = " + url);
                            }
                            processComplete(jobInfo, MyErrorCode.SUCCESS.getCode(), null);
                        } else {
                            String msg = message + "error:" + response.getStatusLine().getReasonPhrase();
                            logger.warn(msg);
                            processComplete(jobInfo, statusCode, msg);

                        }
                    } finally {
                        HttpClientUtils.closeQuietly(response);
                    }

                }

                @Override
                public void failed(final Exception ex) {
                    String msg = message + "error:" + ex.getMessage();
                    logger.error(message, ex);
                    processComplete(jobInfo, MyErrorCode.INTERNAL_SERVER_ERROR.getCode(), msg);
                }

                @Override
                public void cancelled() {
                    String msg = message + "cancelled";
                    logger.info(msg);
                    processComplete(jobInfo, MyErrorCode.INTERNAL_SERVER_ERROR.getCode(), msg);
                }

            });

        } catch (Exception e) {
            logger.error(message, e);
            String msg = message + e.getMessage();
            processComplete(jobInfo, MyErrorCode.INTERNAL_SERVER_ERROR.getCode(), msg);

        }
    }//end asyncRequest

    /**
     * 同步请求。
     * @param jobInfo
     * @param method
     * @param url
     */
    private void syncRequest(final JobInfo jobInfo, final String method, final String url) {
        String message = "sync request error: url = " + url + ".";
        CloseableHttpResponse response = null;
        try {
            HttpUriRequest request = null;
            if (StringUtils.isBlank(method) || method.equalsIgnoreCase("get")) {
                request = new HttpGet(url);
            } else {
                request = new HttpPost(url);
            }

            response = httpclient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == HttpStatus.SC_OK) {
                if(logger.isDebugEnabled()) {
                    logger.debug("sync request success: url = " + url );
                    //logger.debug("返回数据：" + json);
                }
                processComplete(jobInfo, MyErrorCode.SUCCESS.getCode(), null);
            } else {
                message += "error:" + response.getStatusLine().getReasonPhrase();
                logger.warn(message);
                processComplete(jobInfo, statusCode, message);
            }


        } catch (ConnectException e) {
            logger.error(message, e);
            processComplete(jobInfo, MyErrorCode.SERVICE_UNAVAILABLE.getCode(), message + e.getMessage());

        } catch (ClientProtocolException e) {
            logger.error(message, e);
            processComplete(jobInfo, MyErrorCode.INTERNAL_SERVER_ERROR.getCode(), message + e.getMessage());

        } catch (IOException e) {
            logger.error(message, e);
            processComplete(jobInfo, MyErrorCode.INTERNAL_SERVER_ERROR.getCode(), message + e.getMessage());
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }//end syncRequest

}
