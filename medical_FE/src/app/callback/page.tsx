'use client'
import React, { useEffect } from "react";
import { useRouter } from "next/navigation";
export default function OAuth2Callback() {

    const router = useRouter();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get("token"); // URL에서 token 추출
        const username = params.get("username");

        if (token && username) {
            sessionStorage.setItem("jwtToken", "Bearer " + token);
            sessionStorage.setItem("username", username);
            sessionStorage.setItem("role", "ROLE_MEMBER");

            const fetchCallback = async () => {

                try {
                    const resp = await fetch(`https://nonefficient-lezlie-progressively.ngrok-free.dev/api/getMember/${username}`, {
                        headers: { 
                            "Authorization": "Bearer " + token,
                            "ngrok-skip-browser-warning": "true" // ngrok 경고창 방지
                        }
                    });
                    const data = await resp.json();
                    sessionStorage.setItem('alias', data.alias);
                    
                    alert("로그인 성공!");
                    router.push('/medicalInfo');
                } catch (err) {
                    // alias 정보 못가져와도 로그인은 된 상태이므로 이동 가능
                    router.push('/medicalInfo');
                }
            };
            fetchCallback();
        } else {
            alert("인증 실패: 토큰이 없습니다.");
            router.push("/");
        }
    }, [router]);
    return <p>로그인 처리 중...</p>;
};
