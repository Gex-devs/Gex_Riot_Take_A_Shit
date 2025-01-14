package com.gex.gex_riot_take_a_shit;

import android.util.Log;

import com.gex.gex_riot_take_a_shit.Background.WebsocketServer;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocalApiHandler {
    private static LocalApiHandler instance;
    private static OkHttpClient client;
    LocalApiHandler() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Create an SSL context with the custom trust manager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .build();

    }


    public static LocalApiHandler getInstance() throws NoSuchAlgorithmException, KeyManagementException {

        if (instance == null) {
            synchronized (LocalApiHandler.class) {
                if (instance == null) {
                    instance = new LocalApiHandler();
                }
            }
        }
        return instance;
    }

    public static String current_state() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    Request request = new Request.Builder()
                            .url("http:/" + String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0] + ":7979/api/current_state")
                            .build();
                    Response response = client.newCall(request).execute();
                    String reponsebody = response.body().string();
                    Log.d("Local Api, current_state", "call: "+reponsebody);
                    return reponsebody;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Api_Call", "Call failed: " + e);
                    return "null";
                }
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();
    }

    public static void send_text(String text) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/sendChat?text="+text)
                            .build();

                    Response response = client.newCall(request).execute();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getUsername(String puuid) throws IOException, ExecutionException, InterruptedException {

        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/get_name?PUID="+puuid)
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();

    }
    public static String getMyID() throws IOException, ExecutionException, InterruptedException {

        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/GetMyID")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();

    }
    public static boolean StartQ() throws IOException, ExecutionException, InterruptedException {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/startQ")
                            .build();

                    Response response = client.newCall(request).execute();
                    return response.isSuccessful();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();
    }
    public static void LeaveQ() throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/stopQ")
                            .build();

                    Response response = client.newCall(request).execute();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void LeaveParty() throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/leave_party")
                            .build();

                    Response response = client.newCall(request).execute();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void Dodge() throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/Dodge")
                            .build();

                    Response response = client.newCall(request).execute();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void Change_Q(String Queue) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/changeQ?queue="+Queue)
                            .build();

                    Response response = client.newCall(request).execute();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void set_party_status(String status) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/party_accessibility?state="+status)
                            .build();

                    Response response = client.newCall(request).execute();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static boolean SelectAgent(String agent) throws IOException, ExecutionException, InterruptedException {
        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/pregame/selectagent?agent="+agent)
                            .build();

                    Response response = client.newCall(request).execute();
                    return response.isSuccessful();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();
    }

    public static String LockAgent(String agent) throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/pregame/lockagent?agent="+agent)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();

    }
    public static String get_map_name() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/get_map")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();

    }
    public static String get_server() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    

                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/get_server/pre_game")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();

    }
    public static String get_gamemode() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/pregame/gamemode")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();
    }
    public static String getPreGame() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");

                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/pregame")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();
    }
    public static String get_players_Current_game() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/coregame/players")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();
    }
    public static String get_party() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/getParty")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();

    }
    public static String GetQeueMode() throws IOException, ExecutionException, InterruptedException {
        Callable<String> callable = new Callable<String>() {
            public String call() {
                try {
                    System.out.println("called from python Rest Api");
                    // code request code here
                    Request request = new Request.Builder()
                            .url("http:/"+String.valueOf(WebsocketServer.getInstance().getRemoteSocketAddress()).split(":")[0]+":7979/api/QeueMode")
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Api Call Response", "call: "+response);
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "null";
                }

            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the Callable object to the ExecutorService to run in a separate thread
        return executor.submit(callable).get();

    }
}
