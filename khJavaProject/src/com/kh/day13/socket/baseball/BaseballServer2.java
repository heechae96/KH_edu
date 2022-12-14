package com.kh.day13.socket.baseball;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class BaseballServer2 {

	// 강사님 방식
	// 받은 값이 numbers의 값과 비교했을 때
	// 숫자가 맞고 위치도 맞는지 -> strike
	// 숫자는 맞는데 위치는 틀린지 -> ball
	// 아무것도 맞지 않았는지를
	// 스트라이크, 볼로 출력해준다
	public static void main(String[] args) {

		ServerSocket ss = null;
		int port = 8589;

		InputStream is = null;
		OutputStream os = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;

		int[] numbers = new int[3];
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		Date date = new Date();
		
		try {
			System.out.println("서버소켓을 생성했습니다");
			ss = new ServerSocket(port);
			Thread.sleep(2000);
			System.out.println(sdf.format(date));
			System.out.println("클라이언트의 접속을 기다립니다");
			Socket socket = ss.accept();
			System.out.println("클라이언트가 접속했습니다");
			is = socket.getInputStream();
			os = socket.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);

			Random rand = new Random();

			// ★★★ 로또 프로그램에서 사용했던 방법 ★★★
			// 번호 3개를 뽑은 후에 게임 준비해야함
			for (int i = 0; i < numbers.length; i++) {
				// 1부터 9사이의 랜덤한 숫자
				numbers[i] = rand.nextInt(9) + 1;
				// 중복제거
				for (int e = 0; e < i; e++) {
					if (numbers[e] == numbers[i]) {
						i--;
						break;
					}
				}
			}
			System.out.println("서버 숫자 -> " + numbers[0] + " " + numbers[1] + " " + numbers[2]);
			System.out.println("===== 서버 준비 완료 =====");

			while (true) {
				// 값 받기
				String readNum = dis.readUTF();
				System.out.println("받은값: " + readNum);

				// 넘어온값은 String. 비교하려면 문자열 배열이 편리
				String[] readUpdateNum = readNum.split(" ");

				// 인덱스랑 값 둘 다 비교해야함
				// -> 중첩 for문 필요
				int strike = 0;
				int ball = 0;

				for (int i = 0; i < numbers.length; i++) {
					for (int j = 0; j < readUpdateNum.length; j++) {
						// 먼저 값이 같은지 비교
						if (numbers[i] == Integer.parseInt(readUpdateNum[j])) {
							// 그리고 위치가 같은지 비교를 한다
							if (i == j) {
								strike++;
							} else {
								ball++;
							}
						}
					}
				}

				String result = strike + "스트라이크 " + ball + "볼";
				System.out.println(result);
				// 클라이언트로 결과값 보내주기!
				dos.writeUTF(result);

				// 스트라이크가 3이면 게임 종료
				if (strike == 3) {
					System.out.println("스트라이크 아웃!! 게임 종료..");
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dos.close();
				dis.close();
				is.close();
				os.close();
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
