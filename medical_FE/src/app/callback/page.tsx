'use client'
import React, { useEffect } from "react";
import { useRouter } from "next/navigation";
export default function OAuth2Callback() {

    const router = useRouter();

    useEffect(() => {
        const fetchCallback = async () => {
            try {
                const response = await fetch("https://project-hospital.onrender.com/api/jwtcallback", {
                    method: "POST",
                    credentials: "include", // 쿠키포함
                });
                if (response.ok) {
                    const params = new URLSearchParams(window.location.search);
                    const username = params.get("username")

        if (token && username) {
            sessionStorage.setItem("jwtToken", "Bearer " + token);
            sessionStorage.setItem("username", username);
            sessionStorage.setItem("role", "ROLE_MEMBER");

                    const resp = await fetch(`https://project-hospital.onrender.com/api/getMember/${username}`)
                    const data = await resp.json()
                    const alias = data.alias

                try {
                    const resp = await fetch(`https://nonefficient-lezlie-progressively.ngrok-free.dev/api/getMember/${username}`, {
                        headers: { 
                            "Authorization": "Bearer " + token,
                            "ngrok-skip-browser-warning": "true" // ngrok 경고창 방지
                        }
                    });
                    const data = await resp.json();
                    sessionStorage.setItem('alias', data.alias);
                    
                    router.push('/medicalInfo');
                } catch (err) {
                    // alias 정보 못가져와도 로그인은 된 상태이므로 이동 가능
                    router.push('/medicalInfo');
                }
            } catch (err) {
                alert("서버 요청 오류");
                router.push("/");
            }
        };
        fetchCallback();
    }, []);
    return <></>;
};
